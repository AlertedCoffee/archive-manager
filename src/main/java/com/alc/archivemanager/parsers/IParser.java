package com.alc.archivemanager.parsers;

import java.io.IOException;

public interface IParser {
    public String Parse(String filePath) throws Exception;
}
