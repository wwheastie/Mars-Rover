package com.acme.marsrover.service.impl;

import com.acme.marsrover.service.AsyncDownloadService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class AsyncDownloadServiceImpl extends BaseService implements AsyncDownloadService {
    /**
     * Asynchronous
     * Returns a file from a URL in bytes using a String url
     *
     * @param sUrl
     * @return
     * @throws IOException
     */
    @Override
    @Async("processDateServiceThreadPoolTaskExecutor")
    public CompletableFuture<byte[]> download(String sUrl) throws IOException {
        logger.info("Processing on thread: " + Thread.currentThread().getName());

        //Get a valid HttpURLConnection object using the String url
        CompletableFuture<HttpURLConnection> httpURLConnection = getValidConnection(sUrl);

        //Download file using HttpURLConnection object and return file
        return download(httpURLConnection);
    }

    /**
     * Asynchronous
     * Returns a file from a URL in bytes using a HttpURLConnection
     *
     * @param fHttpUrlConnection
     * @return
     */
    @Async("processDateServiceThreadPoolTaskExecutor")
    public CompletableFuture<byte[]> download(CompletableFuture<HttpURLConnection> fHttpUrlConnection) {
        logger.info("Processing on thread: " + Thread.currentThread().getName());

        //Create input stream
        CompletableFuture<InputStream> fInputStream = createInputStream(fHttpUrlConnection);

        //Read from input stream and return byte array
        return readToByteArrayStream(fInputStream);
    }

    /**
     * Asynchronous
     * Creates a valid HttpURLConnection
     *
     * @param sUrl
     * @return
     * @throws IOException
     */
    @Async("processDateServiceThreadPoolTaskExecutor")
    public CompletableFuture<HttpURLConnection> getValidConnection(String sUrl)
            throws IOException {
        //Create URL object from String url
        URL url = createURL(sUrl);

        //Create HttpURLConnection object from URL
        HttpURLConnection httpURLConnection = createHttpUrlConnection(url);

        //Check response status of connection
        if(httpURLConnection.getResponseCode() == HttpStatus.OK.value()) {
            return CompletableFuture.completedFuture(httpURLConnection);
        } else if (httpURLConnection.getResponseCode() == HttpStatus.MOVED_PERMANENTLY.value()) {
            String sAltUrl = httpURLConnection.getHeaderField("Location");
            logger.info("URL has been moved to " + sAltUrl);
            url = createURL(sAltUrl);
            httpURLConnection = createHttpUrlConnection(url);
            return CompletableFuture.completedFuture(httpURLConnection);
        } else {
            String erroMessage = "Could not create valid connection for " + sUrl;
            logger.info(erroMessage);
            throw new IOException(erroMessage);
        }
    }

    /**
     * Creates a URL object
     *
     * @param sUrl
     * @return
     * @throws MalformedURLException
     */
    private URL createURL(String sUrl)
            throws MalformedURLException {
        logger.info("Creating URL object from " + sUrl);
        URL url = new URL(sUrl);
        logger.info("URL created successfully");
        return url;
    }

    /**
     * Creates a HttpURLConnection object
     *
     * @param url
     * @return
     * @throws IOException
     */
    private HttpURLConnection createHttpUrlConnection(URL url)
            throws IOException {
        logger.info("Creating HttpUrlConnection from " + url.getPath());
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        logger.info("HttpUrlConnection created successfully");
        return httpURLConnection;
    }

    /**
     * Asynchronous
     * Creates an InputStream object
     *
     * @param fHttpUrlConnection
     * @return
     */
    @Async("processDateServiceThreadPoolTaskExecutor")
    private CompletableFuture<InputStream> createInputStream(CompletableFuture<HttpURLConnection> fHttpUrlConnection) {
        return fHttpUrlConnection.thenApply(httpUrlConnection -> {
            logger.info("Creating input stream for " + httpUrlConnection.getURL().getPath());
            InputStream is = null;
            try {
                is = httpUrlConnection.getInputStream();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
            logger.info("Input stream created successfully");
            return is;
        });
    }

    /**
     * Asynchronous
     * Reads an Input Stream object and returns the bytes after reading (Asynchronous)
     *
     * @param fInputStream
     * @return
     */
    @Async("processDateServiceThreadPoolTaskExecutor")
    private CompletableFuture<byte[]> readToByteArrayStream(CompletableFuture<InputStream> fInputStream) {
        return fInputStream.thenApply(inputStream -> {
            //Create new output stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //Create bytes to return
            byte[] bytes;

            //Read bytes
            logger.info("Reading bytes");
            byte[] buffer = new byte[1024];
            int length;
            try {
                while((length = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }
            } catch (Exception e) {
                logger.error("Error while reading input", e);
                throw new CompletionException(e);
            }
            logger.info("Read all bytes successfully");

            //Set byte array to return
            bytes = bos.toByteArray();

            //Close output stream
            try {
                bos.close();
            } catch (IOException e) {
                throw new CompletionException(e);
            }

            return bytes;
        });
    }
}
