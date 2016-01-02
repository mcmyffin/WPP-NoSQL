package de.mongo.exportData;

import de.server.persistence.result.MessageData;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author dima
 */
public interface IExportData {

    public boolean saveToFile(File f, List<MessageData> list) throws IOException;
}
