package com.acme.marsrover.service;

import com.acme.marsrover.service.impl.AsyncDownloadServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AsyncDownloadServiceImplTest {
    private AsyncDownloadService asyncDownloadService;

    @Before
    public void setUp() {
        asyncDownloadService = new AsyncDownloadServiceImpl();
    }

    @After
    public void tearDown() {
        asyncDownloadService = new AsyncDownloadServiceImpl();
    }

    @Test(expected = Exception.class)
    public void testBadUrl() throws IOException, ExecutionException, InterruptedException {
        String badUrl = "9:\\badUrl\\";
        asyncDownloadService.download(badUrl).get();
    }

    @Test
    public void testValidUrl() throws IOException, ExecutionException, InterruptedException {
        String validUrl = "http://mars.jpl.nasa.gov/msl-raw-images/msss/01622/mcam/1622MR0083270000801246I01_DXXX.jpg";
        byte[] bytes = asyncDownloadService.download(validUrl).get();
        assertNotNull(bytes);
    }

    @Test(expected = Exception.class)
    public void testInvalidConnection() throws ExecutionException, InterruptedException {
        String invalidUrl = "9:\\badUrl\\";
        getFutureHttpUrlConnection(invalidUrl).get();
    }

    @Test
    public void testValidConnection() throws ExecutionException, InterruptedException {
        String validUrl = "http://mars.jpl.nasa.gov/msl-raw-images/msss/01622/mcam/1622MR0083270000801246I01_DXXX.jpg";
        CompletableFuture<HttpURLConnection> fHttpURLConnection = getFutureHttpUrlConnection(validUrl);
        HttpURLConnection httpURLConnection = fHttpURLConnection.get();
        assertNotNull(httpURLConnection);
    }

    @Test
    public void testValidNumberOfBytes() throws ExecutionException, InterruptedException {
        String validUrl = "http://mars.jpl.nasa.gov/msl-raw-images/msss/01622/mcam/1622MR0083270000801246I01_DXXX.jpg";
        CompletableFuture<HttpURLConnection> fHttpURLConnection = getFutureHttpUrlConnection(validUrl);
        int expectedBytes = fHttpURLConnection.get().getContentLength();
        byte[] bytes = asyncDownloadService.download(fHttpURLConnection).get();
        assertEquals(expectedBytes, bytes.length);
    }

    private CompletableFuture<HttpURLConnection> getFutureHttpUrlConnection(String sUrl) throws CompletionException {
        CompletableFuture<HttpURLConnection> fHttpURLConnection =
                CompletableFuture.supplyAsync(() -> {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        URL url = new URL(sUrl);
                        httpURLConnection = (HttpURLConnection)url.openConnection();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return httpURLConnection;
                });
        return fHttpURLConnection;
    }


}
