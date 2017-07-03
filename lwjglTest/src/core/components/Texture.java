package core.components;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private int _id;

    public Texture(String fileName) throws Exception {
        this(loadTexture(fileName));
    }

    public Texture(int id) {
        this._id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, _id);
    }

    public int getId() {
        return _id;
    }

    private static int loadTexture(String fileName) throws Exception {
    	InputStream in = new FileInputStream("Resources/textures/"+fileName);
    	
    	// 해당 경로의 텍스쳐를 로드할 디코더 생성
        PNGDecoder decoder = new PNGDecoder(in);

        // 텍스쳐 내용을 바이트 버퍼로 로드
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        // Opengl 텍스쳐 생성
        int textureId = glGenTextures();
        // 텍스쳐 바인딩
        glBindTexture(GL_TEXTURE_2D, textureId);

        // OpenGL에 RGBA 바이드를 어떻게 풀것인지 알려줌. 지금은 각 요소는 1바이트씩
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // 텍스쳐 이미지 업로드
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // 밉맵 생성
        glGenerateMipmap(GL_TEXTURE_2D);
        
        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(_id);
    }
}