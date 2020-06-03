package com.acme.marsrover.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.CompletableFuture;

public interface AsyncDownloadService {
    CompletableFuture<byte[]> download(String sUrl) throws IOException;
    CompletableFuture<byte[]> download(CompletableFuture<HttpURLConnection> fHttpUrlConnection);
    CompletableFuture<HttpURLConnection> getValidConnection(String sUrl) throws IOException;
}
