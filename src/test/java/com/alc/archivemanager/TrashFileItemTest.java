package com.alc.archivemanager;

import com.alc.archivemanager.model.TrashFileItem;
import com.alc.archivemanager.config.FilePaths;
import com.alc.archivemanager.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TrashFileItemTest {
    @Test
    void getNameWithNonCollisionPrefixTest() throws IOException {
        File file = new File(FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX + "/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.test.pdf");
        file.createNewFile();
        FileUtil.moveToTrashFile(file);

        File trashFile = new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX, file.getName());
        TrashFileItem trashFileItem = new TrashFileItem(trashFile);

        FileUtil.deleteFileFromTrash(trashFile);


        assertThat(trashFileItem.getName()).isEqualTo("test.pdf");
    }

    @Test
    void getNameTest() throws IOException {
        File file = new File(FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX + "/test.pdf");
        file.createNewFile();
        FileUtil.moveToTrashFile(file);

        File trashFile = new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX, file.getName());
        TrashFileItem trashFileItem = new TrashFileItem(trashFile);

        FileUtil.deleteFileFromTrash(trashFile);

        assertThat(trashFileItem.getName()).isEqualTo("test.pdf");
    }

    @Test
    void getNameWithTwoPlusPartsTest() throws IOException {
        File file = new File(FilePaths.MAIN_PATH + FilePaths.STORAGE_SUFFIX + "/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.asd.test.pdf");
        file.createNewFile();
        FileUtil.moveToTrashFile(file);

        File trashFile = new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX, file.getName());
        TrashFileItem trashFileItem = new TrashFileItem(trashFile);

        FileUtil.deleteFileFromTrash(trashFile);

        assertThat(trashFileItem.getName()).isEqualTo("asd.test.pdf");
    }
}
