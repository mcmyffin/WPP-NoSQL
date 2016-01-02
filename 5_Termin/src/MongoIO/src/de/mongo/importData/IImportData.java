package de.mongo.importData;

import de.server.persistence.result.MessageData;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author dima
 */
public interface IImportData {

    public List<MessageData> loadFile(File f) throws IOException;
}
