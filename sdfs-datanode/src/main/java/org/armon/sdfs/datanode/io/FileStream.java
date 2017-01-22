/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.datanode.DataNodeConstants;

public class FileStream {
	private final String fileFullName;
	private final int readMaxSize;

    public FileStream(String fileName) {
    	fileFullName = ConfigUtils.getConfig().get(DataNodeConstants.DATANODE_FILE_PATH, "/tmp/sdfs") + "/" + fileName;
    	readMaxSize = ConfigUtils.getConfig().getInt(DataNodeConstants.DATANODE_FILE_READ_MAX_SIZE, 1024 * 1024);
    }

    public void writeText(String text) throws IOException {
        if (text == null || text.length() == 0) {
            return;
        }
        FileUtils.writeStringToFile(new File(fileFullName), text, StandardCharsets.UTF_8, true);
    }

    public void writeEndFlag() throws IOException {
        writeText(String.valueOf(DataNodeConstants.END_OF_LOG));
    }

    public ReadResult readText(long offset, int maxCharSize) throws IOException {
        if (offset < 0) {
            offset = 0;
        }

        if (maxCharSize <= 0 || maxCharSize > readMaxSize) {
            maxCharSize = readMaxSize;
        }

        RandomAccessFile raf = null;
        try {
        	raf = new RandomAccessFile(fileFullName, "r");
            if (offset > raf.length()) {
                raf.seek(raf.length() - 1);
                int c = raf.read();
                if (c == DataNodeConstants.END_OF_LOG) {
                    return new ReadResult(true, "", raf.length());
                } else {
                    return new ReadResult(false, "", offset);
                }
            }
            raf.seek(offset);

            int readCharSize = 0;
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Channels.newInputStream(raf.getChannel()), StandardCharsets.UTF_8))) {
                char[] charBuffer = new char[1024];
                int readCount = 0;
                int highSurrogateCount = 0;
                while ((readCount = br.read(charBuffer)) > -1) {
                    char[] readChars = readCount == charBuffer.length ? charBuffer : Arrays.copyOf(charBuffer, readCount);
                    for (char c : readChars) {
                        if (c == DataNodeConstants.END_OF_LOG) {
                            return new ReadResult(true, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
                        } else {
                            if (readCharSize < maxCharSize) {
                                sb.append(c);

                                // handle emoji
                                boolean isHighSurrogate = Character.isHighSurrogate(c);
                                if (!isHighSurrogate && highSurrogateCount == 0) {
                                    readCharSize++;
                                } else {
                                    if (!isHighSurrogate && highSurrogateCount == 2) {
                                        readCharSize++;
                                        highSurrogateCount = 0;
                                    }

                                    if (isHighSurrogate) {
                                        highSurrogateCount++;
                                    }
                                }
                            } else {
                                return new ReadResult(false, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
                            }
                        }
                    }
                }
                return new ReadResult(false, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
            }
        } finally {
			if (raf != null) {
				raf.close();
			}
		}
    }

    public ReadResult tail(int size) throws IOException {
    	RandomAccessFile raf = null;
    	try {
    		raf = new RandomAccessFile(fileFullName, "r");
	        if (raf != null && raf.length() >= size) {
	            return readText(raf.length() - size, size);
	        } else if (raf != null && raf.length() < size) {
	            if (raf.length() < readMaxSize) {
	                return readText(0, (int)raf.length());
	            } else {
	                return readText(raf.length() - readMaxSize, readMaxSize);
	            }
	        } else {
	            throw new IOException("open file " + fileFullName + " failed!");
	        }
    	} finally {
    		if (raf != null) {
    			raf.close();
    		}
    	}
    }
}
