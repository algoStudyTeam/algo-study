package algo220418_0424;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 10:34 -
 * 
 * 4x4 공간 한 칸에 물고기 한마리 각 물고기는 번호와 방향 갖고 있다. => 1~16, 같은 번호를 갖는 물고기 없음
 * 
 * 방향은 상하좌우, 대각선
 * 
 * 청소년 상어가 이 공간에 들어가 물고기를 먹으려 한다. (0,0)에 있는 물고기를 먹고, (0,0)에 들어가게 된다.
 * 
 * 상어의 방향 : (0,0)에 있던 물고기의 방향과 같다. 이후 물고기가 이동
 * 
 * 물고기 - 번호가 작은 물고기부터 순서대로 이동 
 * **한칸만 이동 가능 
 * 이동할 수 있는 칸 -> 빈칸, 다른 물고기가 있는 칸 이동할 수
 * 없는 칸 -> 상어가 있거나, 공간의 경계를 넘는 칸
 * 
 * 각 물고기 : 방향이 이동할 수 있는 칸을 향할 때까지 방향을 45도 반시계 회전시킨다. => 이동할 수 있는 칸이 없으면 이동하지
 * 않는다. 다른 물고기가 있는 칸으로 이동할 때는 서로의 위치를 변경
 * 
 * 상어 - 방향에 있는 칸으로 이동 가능 **여러칸 이동 가능 물고기가 있는 칸으로 이동했다면, 칸에 있는 물고기 먹고 그 물고기의 방향을
 * 가지게 된다.
 * 
 * 이동하는 중에 지나가는 칸에 있는 물고기는 먹지 않는다. 물고기가 없는 칸으로는 이동할 수 없다. 상어가 이동할 수 있는 칸이 없으면
 * 공간에서 벗어나 집으로 간다.
 * 
 * ** 상어가 먹을 수 있는 물고기 번호의 합의 최댓값
 * 
 * ****
 * dfs, 방향 바꾸기 유의
 * 
 */
class Fish {
	int r;
	int c;
	int dir;
	boolean alive;
	public Fish(int r, int c, int dir, boolean alive) {
		super();
		this.r = r;
		this.c = c;
		this.dir = dir;
		this.alive = alive;
	}
	
}

public class BOJ_Gold2_19236_청소년상어 {

//	static int map[][],
	static int maxEatNumSum;
//	static Fish fishes[];
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int map[][] = new int[4][4];
		Fish fishes[] = new Fish[17];
		
		StringTokenizer st = null;
		
		for (int r = 0; r < 4; r++) {
			st = new StringTokenizer(in.readLine(), " ");

			for (int c = 0; c < 4; c++) {
				int idx = Integer.parseInt(st.nextToken());
				int dir = Integer.parseInt(st.nextToken());

				fishes[idx] = new Fish(r, c, dir, true);
				map[r][c] = idx;
			}
		}
		
		// 1. 상어가 (0,0)으로 들어간다.
		int eatNumSum = map[0][0];
		fishes[map[0][0]].alive = false;
		int sharkR = fishes[map[0][0]].r;
		int sharkC = fishes[map[0][0]].c;
		int sharkDir = fishes[map[0][0]].dir;
		map[0][0] = -1;
		
		// dfs
		dfs(sharkR, sharkC, sharkDir, eatNumSum, map, fishes);
		
		System.out.println(maxEatNumSum);

	}

	private static void dfs(int sharkR, int sharkC, int sharkDir, int eatNumSum, int[][] map, Fish[] fishes) {
//		int copyMap[][] = copyArray(map);
//		Fish copyFishes[] = copyFish(fishes);
		maxEatNumSum = Math.max(maxEatNumSum, eatNumSum);
		
		// 물고기 이동
		moveFish(map, fishes);
		
		// 상어 이동
		int sharkNR = sharkR;
		int sharkNC = sharkC;
		while (true) {
			// 포인트!! 꼭 다시 보기
			int copyMap[][] = copyArray(map);
			Fish copyFishes[] = copyFish(fishes);
			
			sharkNR += dr[sharkDir];
			sharkNC += dc[sharkDir];
			
			if (!checkBoundary(sharkNR, sharkNC)) break;
			if (copyMap[sharkNR][sharkNC] == 0) continue;
			
			// 먹을 수 있다면
			int eatNum = copyMap[sharkNR][sharkNC];
			copyFishes[eatNum].alive = false;
			copyMap[sharkNR][sharkNC] = -1;
			copyMap[sharkR][sharkC] = 0;
			
			dfs(sharkNR, sharkNC, copyFishes[eatNum].dir, eatNumSum + eatNum, copyMap, copyFishes);
		}
	}

	static int dr[] = { 0, -1, -1, 0, 1, 1, 1, 0, -1 };
	static int dc[] = { 0, 0, -1, -1, -1, 0, 1, 1, 1 };
	private static void moveFish(int[][] map, Fish[] fishes) {
		for (int idx = 1; idx <= 16; idx++) {
			if (!fishes[idx].alive) continue;
			
			Fish curFish = fishes[idx];
			
			for (int plus = 0; plus < 8; plus++) {
				int curDir = (curFish.dir + plus) % 8 == 0 ? 8 : (curFish.dir + plus) % 8;
				int nr = curFish.r + dr[curDir];
				int nc = curFish.c + dc[curDir];
				
				if (!checkBoundary(nr, nc) || map[nr][nc] == -1) continue;
				
				if (map[nr][nc] != 0) {
					curFish.dir = curDir;
					int changeNum = map[nr][nc];
					// map 위치 바꾸기
					map[nr][nc] = map[curFish.r][curFish.c];
					map[curFish.r][curFish.c] = changeNum;
					
					// fishes 배열 r,c 바꾸기
					fishes[changeNum].r = curFish.r;
					fishes[changeNum].c = curFish.c;
					curFish.r = nr;
					curFish.c = nc;
				} else {
					curFish.dir = curDir;
					// map 위치 바꾸기
					map[nr][nc] = map[curFish.r][curFish.c];
					map[curFish.r][curFish.c] = 0;
					
					// fishes 배열 r,c 바꾸기
					curFish.r = nr;
					curFish.c = nc;
				}
				break;
			}
			
		}
		
	}

	private static Fish[] copyFish(Fish[] fishesArr) {
		Fish copyFishes[] = new Fish[17];
		
		for (int idx = 1; idx <= 16; idx++) {
			Fish curFish = fishesArr[idx];
			
			copyFishes[idx] = new Fish(curFish.r, curFish.c, curFish.dir, curFish.alive);
		}
		
		return copyFishes;
	}

	private static int[][] copyArray(int[][] arr) {
		int copyArr[][] = new int[4][4];
		
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				copyArr[r][c] = arr[r][c];
			}
		}
		
		return copyArr;
	}

	private static boolean checkBoundary(int r, int c) {
		return r >= 0 && r < 4 && c >= 0 && c < 4;
	}

}
