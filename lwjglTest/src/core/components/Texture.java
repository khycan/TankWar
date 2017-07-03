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
    	
    	// �ش� ����� �ؽ��ĸ� �ε��� ���ڴ� ����
        PNGDecoder decoder = new PNGDecoder(in);

        // �ؽ��� ������ ����Ʈ ���۷� �ε�
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        // Opengl �ؽ��� ����
        int textureId = glGenTextures();
        // �ؽ��� ���ε�
        glBindTexture(GL_TEXTURE_2D, textureId);

        // OpenGL�� RGBA ���̵带 ��� Ǯ������ �˷���. ������ �� ��Ҵ� 1����Ʈ��
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // �ؽ��� �̹��� ���ε�
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // �Ӹ� ����
        glGenerateMipmap(GL_TEXTURE_2D);
        
        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(_id);
    }
}