package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 10:15 - 11:52 -> 1시간 35분
 *
 */
public class BOJ_Gold2_20061_모노미노도미노2 {
	
	static int N, command[][] = new int[10005][3], sumScore;
	static boolean green[][] = new boolean[6][4], blue[][] = new boolean[6][4];

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		N = Integer.parseInt(in.readLine());
		
		for (int idx = 1; idx <= N; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			
			int t = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			
			command[idx][0] = t;
			command[idx][1] = r;
			command[idx][2] = c;
		}
		
		// process
		for (int idx = 1; idx <= N; idx++) {
			int t = command[idx][0];
			int r = command[idx][1];
			int c = command[idx][2];
			
			if (t == 1) {
				doOne(r, c);
			} else if (t == 2) {
				doTwo(r, c);
			} else {
				doThree(r, c);
			}
			
//			printArr();
			// 점수 획득
			getScore();

//			printArr();
			
			// 연한 칸에 있으면 밑으로 내리기
			doLightColor();

//			printArr();
		}
		
		System.out.println(sumScore);
		System.out.println(getAllBlockCnt());

	}

//	private static void printArr() {
//		System.out.println();
//		System.out.println("blue");
//		
//		for (int r = 0; r <= 5; r++) {
//			for (int c = 3; c >= 0; c--) {
//				if (blue[r][c]) System.out.print("■");
//				else System.out.print("□");
//			}
//			System.out.println();
//		}
//		
//		System.out.println();
//		System.out.println("green");
//		
//		for (int r = 0; r <= 5; r++) {
//			for (int c = 0; c <= 3; c++) {
//				if (green[r][c]) System.out.print("■");
//				else System.out.print("□");
//			}
//			System.out.println();
//		}
//	}

	private static int getAllBlockCnt() {
		int cnt = 0;
		for (int r = 0; r <= 5; r++) {
			for (int c = 0; c <= 3; c++) {
				if (blue[r][c]) ++cnt;
				if (green[r][c]) ++cnt;
			}
		}
		
		return cnt;
	}

	private static void doLightColor() {
		// 파란색 맵
		int haveBlockLine = 0;
		for (int r = 0; r <= 1; r++) {
			for (int c = 0; c <= 3; c++) {
				if (blue[r][c]) {
					++haveBlockLine;
					break;
				}
			}
		}
		
		if (haveBlockLine >= 1) {
			for (int r = 5 - haveBlockLine; r >= 0; r--) {
				for (int c = 0; c <= 3; c++) {
					blue[r + haveBlockLine][c] = blue[r][c];
				}
			}
		}
		
		if (haveBlockLine >= 1) {
			Arrays.fill(blue[0], false);
			Arrays.fill(blue[1], false);
		}

		// 초록색 맵
		haveBlockLine = 0;
		for (int r = 0; r <= 1; r++) {
			for (int c = 0; c <= 3; c++) {
				if (green[r][c]) {
					++haveBlockLine;
					break;
				}
			}
		}

		if (haveBlockLine >= 1) {
			for (int r = 5 - haveBlockLine; r >= 0; r--) {
				for (int c = 0; c <= 3; c++) {
					green[r + haveBlockLine][c] = green[r][c];
				}
			}
		}

		if (haveBlockLine >= 1) {
			Arrays.fill(green[0], false);
			Arrays.fill(green[1], false);
		}

	}

	private static void getScore() {
		// 파란색 맵
		int cnt = 0;
		boolean haveScore = false;
		
		while (true) {
			haveScore = false;
			
			for (int r = 2; r <= 5; r++) {
				cnt = 0;
				for (int c = 0; c <= 3; c++) {
					if (blue[r][c]) ++cnt;
				}
				
				if (cnt == 4) {
					haveScore = true;
					++sumScore;
					Arrays.fill(blue[r], false);
					
					// 위에 있는 행들 아래로 내리기
					for (int upR = r; upR > 0; upR--) {
						for (int upC = 0; upC <= 3; upC++) {
							blue[upR][upC] = blue[upR-1][upC];
						}
					}
				}
			}
			
			if (!haveScore) break;
		}
		
		// 초록색 맵
		while (true) {
			haveScore = false;
			
			for (int r = 2; r <= 5; r++) {
				cnt = 0;
				for (int c = 0; c <= 3; c++) {
					if (green[r][c]) ++cnt;
				}
				
				if (cnt == 4) {
					haveScore = true;
					++sumScore;
					Arrays.fill(green[r], false);
					
					// 위에 있는 행들 아래로 내리기
					for (int upR = r; upR > 0; upR--) {
						for (int upC = 0; upC <= 3; upC++) {
							green[upR][upC] = green[upR-1][upC];
						}
					}
				}
			}
			
			if (!haveScore) break;
		}
		
	}

	private static void doThree(int r, int c) {
		// 파란색 맵
		int highR = 6;
		int blueCIdx_1 = r;
		int blueCIdx_2 = r+1;
		
		boolean blueSuccess = false;
		
		for (int blueR = 0; blueR <= 5; blueR++) {
			if (blue[blueR][blueCIdx_1]) {
				highR = Math.min(blueR - 1, highR);
				blueSuccess = true;
				break;
			}
		}
		
		if (!blueSuccess) {
			highR = Math.min(highR, 5);
		}
		
		blueSuccess = false;
		
		for (int greenR = 0; greenR <= 5; greenR++) {
			if (blue[greenR][blueCIdx_2]) {
				highR = Math.min(greenR - 1, highR);
				blueSuccess = true;
				break;
			}
		}

		if (!blueSuccess) {
			highR = Math.min(highR, 5);
		}

		blue[highR][blueCIdx_1] = true;
		blue[highR][blueCIdx_2] = true;

		// 초록색 맵
		int greenCIdx = c;

		boolean greenSuccess = false;
		for (int greenR = 0; greenR <= 5; greenR++) {
			if (green[greenR][greenCIdx]) {
				green[greenR - 1][greenCIdx] = true;
				green[greenR - 2][greenCIdx] = true;
				greenSuccess = true;
				break;
			}
		}

		// 경계까지 갔으면
		if (!greenSuccess) {
			green[5][greenCIdx] = true;
			green[4][greenCIdx] = true;
		}
	}

	private static void doTwo(int r, int c) {
		// 파란색 맵
		int blueCIdx = r;
		
		boolean blueSuccess = false;
		for (int blueR = 0; blueR <= 5; blueR++) {
			if (blue[blueR][blueCIdx]) {
				blue[blueR-1][blueCIdx] = true;
				blue[blueR-2][blueCIdx] = true;
				blueSuccess = true;
				break;
			}
		}

		// 경계까지 갔으면
		if (!blueSuccess) {
			blue[5][blueCIdx] = true;
			blue[4][blueCIdx] = true;
		}
		
		// 초록색 맵
		int highR = 6;
		int greenCIdx_1 = c;
		int greenCIdx_2 = c+1;
		
		boolean greenSuccess = false;
		
		for (int greenR = 0; greenR <= 5; greenR++) {
			if (green[greenR][greenCIdx_1]) {
				highR = Math.min(greenR - 1, highR);
				greenSuccess = true;
				break;
			}
		}
		
		if (!greenSuccess) {
			highR = Math.min(highR, 5);
		}
		
		greenSuccess = false;
		
		for (int greenR = 0; greenR <= 5; greenR++) {
			if (green[greenR][greenCIdx_2]) {
				highR = Math.min(greenR - 1, highR);
				greenSuccess = true;
				break;
			}
		}
		
		if (!greenSuccess) {
			highR = Math.min(highR, 5);
		}
		
		green[highR][greenCIdx_1] = true;
		green[highR][greenCIdx_2] = true;
	}

	private static void doOne(int r, int c) {
		// 파란색 맵
		int blueCIdx = r;
		
		boolean blueSuccess = false;
		for (int blueR = 0; blueR <= 5; blueR++) {
			if (blue[blueR][blueCIdx]) {
				blue[blueR-1][blueCIdx] = true;
				blueSuccess = true;
				break;
			}
		}
		
		// 경계까지 갔으면
		if (!blueSuccess) {
			blue[5][blueCIdx] = true;
		}
		
		// 초록색 맵
		int greenCIdx = c;
		
		boolean greenSuccess = false;
		for (int greenR = 0; greenR <= 5; greenR++) {
			if (green[greenR][greenCIdx]) {
				green[greenR-1][greenCIdx] = true;
				greenSuccess = true;
				break;
			}
		}
		
		if (!greenSuccess) {
			green[5][greenCIdx] = true;
		}
	}


}
