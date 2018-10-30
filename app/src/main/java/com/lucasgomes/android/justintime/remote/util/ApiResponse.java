package com.lucasgomes.android.justintime.remote.util;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

public class ApiResponse<T> {

    private static Pattern LINK_PATTERN =
            Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");

    private static Pattern PAGE_PATTERN =
            Pattern.compile("\\bpage=(\\d+)");

    private static String NEXT_LINK = "next";

    int code = 0;
    Boolean isSuccessful;
    @Nullable
    T body = null;
    @Nullable String errorMessage = null;
    Map<String, String> links = Collections.emptyMap();
    @Nullable Throwable error = null;

    public ApiResponse(Throwable error) {
        code = 500;
        errorMessage = error.getMessage();
        this.error = error;
    }

    public ApiResponse(int code,
                       @Nullable T body,
                       @Nullable String errorMessage,
                       @Nullable Throwable error) {
        this.code = code;
        this.body = body;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public ApiResponse(Response<T> response) {
        this.code = response.code();
        isSuccessful = code >= 200 && code <= 299;

        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            @Nullable String message = null;

            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (message == null) {
                message = response.message();
            }
            errorMessage = message;
        }
        String linkHeader = response.headers().get("link");
        if (linkHeader != null) {
            Matcher matcher = LINK_PATTERN.matcher(linkHeader);
            while (matcher.find()) {
                int count = matcher.groupCount();
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
    }

    @Nullable Integer getNextPage() {
        @Nullable Integer value = null;
        @Nullable String next = links.get(NEXT_LINK);
        if (next != null) {
            Matcher matcher = PAGE_PATTERN.matcher(next);
            if (!matcher.find() || matcher.groupCount()!= 1) {
                value = null;
            } else {
                try {
                    value = Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    value = null;
                }
            }
        }
        return value;
    }
}