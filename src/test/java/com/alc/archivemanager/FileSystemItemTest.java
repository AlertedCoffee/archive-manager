package com.alc.archivemanager;

import com.alc.archivemanager.model.FileSystemItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

@SpringBootTest
public class FileSystemItemTest {
    @Test
    void getNameWithNonCollisionPrefixTest(){
        File file = new File("/storage/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.test.pdf");
        FileSystemItem fileSystemItem = new FileSystemItem(file);

        assertThat(fileSystemItem.getName()).isEqualTo("test.pdf");
    }

    @Test
    void getNameTest(){
        File file = new File("/storage/test.pdf");
        FileSystemItem fileSystemItem = new FileSystemItem(file);

        assertThat(fileSystemItem.getName()).isEqualTo("test.pdf");
    }

    @Test
    void getNameWithTwoPlusPartsTest(){
        File file = new File("/storage/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.asd.test.pdf");
        FileSystemItem fileSystemItem = new FileSystemItem(file);

        assertThat(fileSystemItem.getName()).isEqualTo("asd.test.pdf");
    }
}
