package algo220418_0424;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 7:07 - 8:27
 * 9:55 - 11:13
 * 
 * 2시간 40분...
 * 온풍기 설치
 * 
 * RxC -> 1,1 ~ r,c
 * 
 * 온도 실시간 모니터링 -> (r,c)는 r행 c열 의미
 * 
 * 작업 순차
 * 
 * 집에 있는 모든 온풍기에서 바람이 한 번 나옴
온도가 조절됨
온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
초콜릿을 하나 먹는다.
조사하는 모든 칸의 온도가 K 이상이 되었는지 검사. 모든 칸의 온도가 K이상이면 테스트를 중단하고, 아니면 1부터 다시 시작한다.
 * 
 * 
 * ## 온도 조절 과
 * 모든 인접한 칸에서, 온도가 높은칸-> 낮은칸 : [두 칸의 온도의 차이/4]만큼 온도 조절
 * => 높은 칸은 온도 감소, 낮은 칸은 온도 상승
 * => 벽이 있는 경우, 온도 조절되지 않는다.
 * => 모든 칸에 대해 동시에 발생
 * 
 *
 */

class WarmSystem {
	int r;
	int c;
	int dir;
	
	public WarmSystem(int r, int c, int dir) {
		super();
		this.r = r;
		this.c = c;
		this.dir = dir;
	}
}

public class BOJ_Platinum5_23289_온풍기안녕 {

	static int temper[][], tempTemper[][], R, C, K, W, eatChocolate;
	static boolean upWall[][], rightWall[][], visited[][];
	static List<WarmSystem> warmSystems = new ArrayList<WarmSystem>();
	static List<int[]> checkBlocks = new ArrayList<int[]>();
	static Queue<int[]> queue = new LinkedList<int[]>();
	
	static int[] dr = {0, 0, 0, -1, 1}; // 오왼위아래
	static int[] dc = {0, 1, -1, 0, 0};
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		temper = new int[R+1][C+1];
		tempTemper = new int[R+1][C+1];
		upWall = new boolean[R+1][C+1];
		rightWall = new boolean[R+1][C+1];
		visited = new boolean[R+1][C+1];
		
		for (int r = 1; r <= R; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= C; c++) {
				int cur = Integer.parseInt(st.nextToken());
				
				if (cur >= 1 && cur <= 4) {
					warmSystems.add(new WarmSystem(r, c, cur));
				} else if (cur == 5) {
					checkBlocks.add(new int[] {r, c});
				}
			}
		}
		
		W = Integer.parseInt(in.readLine());
		
		for (int i = 0; i < W; i++) {
			st = new StringTokenizer(in.readLine(), " ");
			
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int t = Integer.parseInt(st.nextToken());
			
			if (t == 0) {
				upWall[r][c] = true;
			} else {
				rightWall[r][c] = true;
			}
		}
		
		while (true) {
			if (eatChocolate > 100) break;
//			집에 있는 모든 온풍기에서 바람이 한 번 나옴
			windAllWarmSystems();
//			온도가 조절됨
			adjustTemper();
//			온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
			aroundMinusOneDegree();
//			초콜릿을 하나 먹는다.
			++eatChocolate;
//			조사하는 모든 칸의 온도가 K 이상이 되었는지 검사. 모든 칸의 온도가 K이상이면 테스트를 중단하고, 아니면 1부터 다시 시작한다.
			if (checkK()) break;
		}
		
		System.out.println(eatChocolate);
	}
	
	private static boolean checkK() {
		for (int[] pos : checkBlocks) {
			if (temper[pos[0]][pos[1]] < K) return false;
		}
		
		return true;
	}
	
	private static void aroundMinusOneDegree() {
		
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				if (r == 1 || r == R || c == 1 || c == C) {
					if (temper[r][c] >= 1) {
						--temper[r][c];
					}
				}
			}
		}
		
	}
	private static void adjustTemper() {
		initTempTemper();
		
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				if (temper[r][c] == 0) continue;
				for (int dir = 1; dir <= 4; dir++) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];

					if (!checkBoundary(nr, nc) || (temper[r][c] <= temper[nr][nc]))
						continue;

					if ((dir == 1 && !rightWall[r][c]) || (dir == 2 && !rightWall[nr][nc])
							|| (dir == 3 && !upWall[r][c]) || (dir == 4 && !upWall[nr][nc])) {
						calTwoNumber(r, c, nr, nc);
					}
				}
			}
		}
		
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				temper[r][c] += tempTemper[r][c];
			}
		}
	}
	
	private static void calTwoNumber(int curR, int curC, int nr, int nc) {
		
		int differ = (temper[curR][curC] - temper[nr][nc]) / 4;
		
		tempTemper[curR][curC] -= differ;
		tempTemper[nr][nc] += differ;
	}
	
	private static void windAllWarmSystems() {
		for (WarmSystem warmSystem : warmSystems) {
			initTempTemper();
			queue.clear();
			
			if (warmSystem.dir == 1) {
				windRight(warmSystem);
			} else if (warmSystem.dir == 2) {
				windLeft(warmSystem);
			} else if (warmSystem.dir == 3) {
				windUp(warmSystem);
			} else {
				windDown(warmSystem);
			}
			
			sumTemper();
		}
		
	}
	
	private static void windDown(WarmSystem warmSystem) {
		int startR = warmSystem.r + 1;
		int startC = warmSystem.c;
		
		tempTemper[startR][startC] = 5;
		queue.offer(new int[] {startR, startC});
		
		int curTemper = 4;
		while (!queue.isEmpty()) {
			int size = queue.size();
			
			for (int a = 0; a < size; a++) {
				int curR = queue.peek()[0];
				int curC = queue.peek()[1];
				queue.poll();
				
				// 아래왼쪽
				int nr = curR + 1;
				int nc = curC - 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR+1][curC-1] && !rightWall[curR][curC-1]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 아래
				nr = curR + 1;
				nc = curC;
					
				if (checkBoundary(nr, nc) && !upWall[curR+1][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 아래오른쪽
				nr = curR + 1;
				nc = curC + 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR+1][curC+1] && !rightWall[curR][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
			}
			
			if (--curTemper == 0) {
				break;
			}
		}
	}
	private static void windUp(WarmSystem warmSystem) {
		int startR = warmSystem.r - 1;
		int startC = warmSystem.c;
		
		tempTemper[startR][startC] = 5;
		queue.offer(new int[] {startR, startC});
		
		int curTemper = 4;
		while (!queue.isEmpty()) {
			int size = queue.size();
			
			for (int a = 0; a < size; a++) {
				int curR = queue.peek()[0];
				int curC = queue.peek()[1];
				queue.poll();
				
				// 위왼쪽
				int nr = curR - 1;
				int nc = curC - 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR][curC-1] && !rightWall[curR][curC-1]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 위
				nr = curR - 1;
				nc = curC;
					
				if (checkBoundary(nr, nc) && !upWall[curR][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 위 오른쪽
				nr = curR - 1;
				nc = curC + 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR][curC+1] && !rightWall[curR][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
			}
			
			if (--curTemper == 0) {
				break;
			}
		}
	}
	private static void windLeft(WarmSystem warmSystem) {
		int startR = warmSystem.r;
		int startC = warmSystem.c - 1;
		
		tempTemper[startR][startC] = 5;
		queue.offer(new int[] {startR, startC});
		
		int curTemper = 4;
		while (!queue.isEmpty()) {
			int size = queue.size();
			
			for (int a = 0; a < size; a++) {
				int curR = queue.peek()[0];
				int curC = queue.peek()[1];
				queue.poll();
				
				// 왼쪽위
				int nr = curR - 1;
				int nc = curC - 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR][curC] && !rightWall[curR-1][curC-1]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 오른쪽
				nr = curR;
				nc = curC - 1;
					
				if (checkBoundary(nr, nc) && !rightWall[curR][curC-1]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 오른쪽아래
				nr = curR + 1;
				nc = curC - 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR+1][curC] && !rightWall[curR+1][curC-1]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
			}
			
			if (--curTemper == 0) {
				break;
			}
		}
	}
	
	private static void windRight(WarmSystem warmSystem) {
		int startR = warmSystem.r;
		int startC = warmSystem.c + 1;
		
		tempTemper[startR][startC] = 5;
		queue.offer(new int[] {startR, startC});
		
		int curTemper = 4;
		while (!queue.isEmpty()) {
			int size = queue.size();
			
			for (int a = 0; a < size; a++) {
				int curR = queue.peek()[0];
				int curC = queue.peek()[1];
				queue.poll();
				
				// 오른쪽위
				int nr = curR - 1;
				int nc = curC + 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR][curC] && !rightWall[curR-1][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 오른쪽
				nr = curR;
				nc = curC + 1;
					
				if (checkBoundary(nr, nc) && !rightWall[curR][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
				
				// 오른쪽아래
				nr = curR + 1;
				nc = curC + 1;
					
				if (checkBoundary(nr, nc) && !upWall[curR+1][curC] && !rightWall[curR+1][curC]) {
					tempTemper[nr][nc] = curTemper;
					queue.offer(new int[] {nr, nc});
				}
			}
			
			if (--curTemper == 0) {
				break;
			}
		}
		
	}
	
	private static void sumTemper() {
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				temper[r][c] += tempTemper[r][c];
			}
		}
		
	}
	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=R && c>=1 && c<=C;
	}
	private static void initTempTemper() {
		for (int r = 1; r <= R; r++) {
			Arrays.fill(tempTemper[r], 0);
		}
	}

}
