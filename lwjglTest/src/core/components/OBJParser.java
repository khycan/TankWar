package core.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJParser {
	public static Mesh LoadData(String f) throws IOException {
		
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> texcoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<FaceType> faces = new ArrayList<>();
		
		int vertexIndex, texcoordIndex, normalIndex, faceIndex;
		int vIndex, tIndex, nIndex;
		
		// 파일 읽기 시작
		BufferedReader br = new BufferedReader(new FileReader("Resources/models/"+f));
		while(true) {
			String line = br.readLine();
			if(line == null) break;
			
			// IndexOutOfBoundsException 발생가능
			try {
				String[] tokens = line.split(" ");
				
				switch (tokens[0]) {
				case "v":
					Vector3f vec3f = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					vertices.add(vec3f);
					break;
				case "vt":
					Vector2f vec2f = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
					texcoords.add(vec2f);
					break;
				case "vn":
					Vector3f vec3fNorm = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					normals.add(vec3fNorm);
					break;
				case "f":
					for(int i = 1; i<tokens.length; i++) {
						FaceType ft = new FaceType();
						
						String[] faceTokens = tokens[i].split("/");
						ft.vIndex1 = Integer.parseInt(faceTokens[0]) - 1;
						if(faceTokens[1].equals("")) {
							ft.vIndex1 = 0;
						} else {
							ft.tIndex1 = Integer.parseInt(faceTokens[1]) - 1;
						}
						ft.nIndex1 = Integer.parseInt(faceTokens[2]) - 1;
						
						faces.add(ft);
					}
					break;
				}
			} catch (IndexOutOfBoundsException e) {
				
			}
		}
		
		// 파일 읽기 종료
		br.close();
		
		return buildMesh(vertices, texcoords, normals, faces);
	}
	
	// z값 반전하여 모델 로딩용
	public static Mesh LoadData(String f, int zsign) throws IOException {
		
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> texcoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<FaceType> faces = new ArrayList<>();
		
		int vertexIndex, texcoordIndex, normalIndex, faceIndex;
		int vIndex, tIndex, nIndex;
		
		// 파일 읽기 시작
		BufferedReader br = new BufferedReader(new FileReader("Resources/models/"+f));
		while(true) {
			String line = br.readLine();
			if(line == null) break;
			
			// IndexOutOfBoundsException 발생가능
			try {
				String[] tokens = line.split(" ");
				
				switch (tokens[0]) {
				case "v":
					Vector3f vec3f = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), -Float.parseFloat(tokens[3]));
					vertices.add(vec3f);
					break;
				case "vt":
					Vector2f vec2f = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
					texcoords.add(vec2f);
					break;
				case "vn":
					Vector3f vec3fNorm = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					normals.add(vec3fNorm);
					break;
				case "f":
					for(int i = 1; i<tokens.length; i++) {
						FaceType ft = new FaceType();
						
						String[] faceTokens = tokens[i].split("/");
						ft.vIndex1 = Integer.parseInt(faceTokens[0]) - 1;
						if(faceTokens[1].equals("")) {
							ft.vIndex1 = 0;
						} else {
							ft.tIndex1 = Integer.parseInt(faceTokens[1]) - 1;
						}
						ft.nIndex1 = Integer.parseInt(faceTokens[2]) - 1;
						
						faces.add(ft);
					}
					break;
				}
			} catch (IndexOutOfBoundsException e) {
				
			}
		}
		
		// 파일 읽기 종료
		br.close();
		
		return buildMesh(vertices, texcoords, normals, faces);
	}
	
	private static Mesh buildMesh(List<Vector3f> vertices, List<Vector2f> textCoords, List<Vector3f> normals, List<FaceType> faces) {
		List<Integer> indices = new ArrayList();
		
		// 정점 배열 생성
		float[] vertexArray = new float[vertices.size() * 3];
		int i = 0;
		for (Vector3f position : vertices) { // 배열로 정점 복사
			vertexArray[i*3] = position.x;
			vertexArray[i*3 + 1] = position.y;
			vertexArray[i*3 + 2] = position.z;
			i++;
		}
		
		float[] textCoordArray = new float[vertices.size() * 2];
		float[] normalArray = new float[vertices.size() * 3];
		
		if (textCoords.size() <= 0) { // 텍스쳐좌표가 없을 경우
			textCoords.add(new Vector2f(0.0f, 0.0f));
		}
		// 면 데이터에서 인덱스와 텍스쳐, 법선 배열 뽑아냄
		for(FaceType face : faces) {
			indices.add(face.vIndex1);
			Vector2f textCoord = textCoords.get(face.tIndex1);
			textCoordArray[face.vIndex1 * 2] = textCoord.x;
			textCoordArray[face.vIndex1 * 2 + 1] = 1 - textCoord.y;
			
			Vector3f normal = normals.get(face.nIndex1);
			normalArray[face.vIndex1 * 3] = normal.x;
			normalArray[face.vIndex1 * 3 + 1] = normal.y;
			normalArray[face.vIndex1 * 3 + 2] = normal.z;
			
		}
		int[] indicesArray = new int[indices.size()];
		indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
		return new Mesh(vertexArray, textCoordArray, normalArray, indicesArray);
	}
	
	// 면 타입
	public static class FaceType {
		public int vIndex1;
		public int tIndex1;
		public int nIndex1;
		
		public FaceType() {
			vIndex1 =  0;
			tIndex1 =  0;
			nIndex1 =  0;
		}
	}
}
