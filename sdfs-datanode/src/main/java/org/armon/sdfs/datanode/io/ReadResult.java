/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.io;

public class ReadResult {
	private String content = "";
    private boolean isEnd = false;
    private long offset = 0;

    public ReadResult(boolean isEnd, String content, long offset) {
        this.content = content;
        this.isEnd = isEnd;
        this.offset = offset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
