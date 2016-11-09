package gov.uspto.patent.bulk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DumpFileAps extends DumpFile {
	private static final Logger LOGGER = LoggerFactory.getLogger(DumpFileAps.class);

	private int currentRecCount;
	
	private static final String startTag = "PATN";

	public DumpFileAps(File file) {
		super(file);
	}
	
    public DumpFileAps(String name, BufferedReader reader) {
        super(name, reader);
    }

	@Override
	public String read() {
		StringBuilder content = new StringBuilder();

		try {
			boolean startTagSeen = false;
			String line;
			while (super.getReader().ready() && (line = super.getReader().readLine()) != null) {
				if (line.startsWith(startTag)) {
					if (startTagSeen) {
						currentRecCount++;
						return startTag + "\n" + content.toString();
					}

					content = new StringBuilder();
					startTagSeen = true;
				} else {
					content.append(line).append('\n');
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while reading file: {}:{}", super.getFile(), currentRecCount, e);
		}

        if (content.length()==0){
            return null;
        }
        else { 
            return startTag + "\n" + content.toString();
        }
	}

	@Override
	public void skip(int skipCount) throws IOException {
		for (int i = 1; i < skipCount; i++) {
			super.next();
			currentRecCount++;
		}
	}

	@Override
	public int getCurrentRecCount() {
		return currentRecCount;
	}

}
