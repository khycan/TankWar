package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {
	public static String loadResource(String fileName) throws Exception {
        String result = new String("");
        
        FileInputStream input = null;
        try{
            File file = new File(fileName);
                     
            input = new FileInputStream(file);
            int readBuffer = 0;
            byte[] buffer = new byte[512];
            while((readBuffer = input.read(buffer)) != -1) {
                result += new String(buffer, 0, readBuffer);
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try{
                // 생성된 InputStream Object를 닫아준다.
                input.close();
            } catch(IOException io) {}
        }
        
        return result;
    }
}
