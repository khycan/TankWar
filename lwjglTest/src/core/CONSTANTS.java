package core;

public final class CONSTANTS {
	public final class OBJECT_DRAWMODE {
		public static final int NON_DRAW = 0;
		public static final int REGULAR = 1;
	}
	public final class CAMERA_NEEDS {
		public static final float Z_NEAR = 0.01f;
		public static final float Z_FAR = 1500.0f;
	}
	public final class MESSAGE_TYPE {
		public static final int TANK_INFO = 0;
	}
	
	public final class ENEMY_ACTION {
		public static final int NOTHING = -1;
		public static final int ROTATE_CLOCK = 0;
		public static final int ROTATE_ANTICLOCK = 1;
		public static final int MOVE_FORWARD = 2;
		public static final int MOVE_BACK = 3;
	}
}
