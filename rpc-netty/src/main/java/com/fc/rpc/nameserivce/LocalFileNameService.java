package com.fc.rpc.nameserivce;

import com.fc.rpc.serialize.SerializeSupport;
import com.fc.rpc.spi.NameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author since
 * @date 2025-01-24 14:20
 */
public class LocalFileNameService implements NameService {
    private static final Logger logger = LoggerFactory.getLogger(LocalFileNameService.class);
    private static final Collection<String> schemes = Collections.singleton("file");
    private File file;

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (schemes.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new RuntimeException("Unsupported  scheme: " + nameServiceUri.getScheme());
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI uri) throws IOException {
        logger.info("register service {}, url: {} ", serviceName, uri);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                Metadata metadata;
                byte[] bytes;
                if (fileLength > 0) {
                    bytes = new byte[(int) raf.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }

                    metadata = SerializeSupport.parse(bytes);
                } else {
                    metadata = new Metadata();
                }
                List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
                }
                logger.info(metadata.toString());

                bytes = SerializeSupport.serialize(metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }
}
