package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * 5:39 - 7:10 = 1시간 30분 소요
 * 
 * 체크를 중간중간 계속 해줘야 함!!
 * 
 * 체스판, 말 -> 새로운 게임
 * 
 * NxN체스판, 말의 개수 K개
 * 말 위에 다른 말 올릴 수 있다.
 * 
 * 체스판 -> 흰색, 빨간색, 파란색 중 하나로 색칠
 * 
 * 체스판 위에 말 K개 놓고 시작 -> 말 번호 : 1~K번, 이동 방향도 정해져 있음 (위아왼오)
 * 
 * 턴 한번
 * 1~K말 순서대로 이동
 * => 한 말이 이동할 때, 위에 올려져 있는 말도 함께 이동.
 * 
 * - 말의 이동 방향에 있는 칸에 따라서 말의 이동이 다름.
 * 
 * 말이 4개 이상 쌓이는 순간 게임 종료.
 * 
 * 이동하려는 칸이
 * 흰색 -> 그 칸으로 이동 or 말 올림
 * 빨간색 -> 이동하는 말들의 쌓여있는 순서를 전부 반대로 바꾼다.
 * 파란색 -> 이동 방향을 반대로 하고 한 칸 이동
 * 	- 이동하려는 칸이 파란색인 경우, 가만히 있는다.
 *  - 체스판 벗어나는 경우도 같은 경우
 *
 */
class Horse {
	int order;
	int r;
	int c;
	int dir;
	
	public Horse(int order, int r, int c, int dir) {
		super();
		this.order = order;
		this.r = r;
		this.c = c;
		this.dir = dir;
	}
}

public class BOJ_Gold2_17837_새로운게임2 {

	static int colorMap[][], N, K, turn;
	static Horse horses[];
	static List<Horse> horseMap[][];
	static int[] dr = {0, 0,0,-1,1};
	static int[] dc = {0, 1,-1,0,0};
	static Stack<Horse> stack = new Stack<Horse>();
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		colorMap = new int[N+1][N+1];
		horses = new Horse[K+1];
		horseMap = new ArrayList[N+1][N+1];
		
		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= N; c++) {
				colorMap[r][c] = Integer.parseInt(st.nextToken());
				horseMap[r][c] = new ArrayList<Horse>();
			}
		}
		
		for (int k = 1; k <= K; k++) {
			st = new StringTokenizer(in.readLine(), " ");
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			horses[k] = new Horse(k, r, c, dir);
			horseMap[r][c].add(horses[k]);
		}
		
		while (true) {
			++turn;
			
			if (turn > 1000) {
				System.out.println(-1);
				return;
			}
			
			// 1. 순서대로 말 이동
			if (moveHorses()) {
				break;
			}
		}
		
		System.out.println(turn);
	}

	
	private static boolean moveHorses() {
		Horse curHorse;
		
		boolean isFinished = false;
		
		for (int curOrder = 1; curOrder <= K; curOrder++) {
			curHorse = horses[curOrder];
			
			int nr = curHorse.r + dr[curHorse.dir];
			int nc = curHorse.c + dc[curHorse.dir];
			
			if (!checkBoundary(nr, nc)) {
				curHorse.dir = reverseDir(curHorse.dir);
				
				nr = curHorse.r + dr[curHorse.dir];
				nc = curHorse.c + dc[curHorse.dir];
				
				if (colorMap[nr][nc] == 0) {
					moveToWhite(curHorse.r, curHorse.c, nr, nc, curOrder);
				} else if (colorMap[nr][nc] == 1) {
					moveToRed(curHorse.r, curHorse.c, nr, nc, curOrder);
				}
				// 파란색인 경우에는 이동하지 않고 가만히 있는다.
				
			} else if (colorMap[nr][nc] == 0) {
				moveToWhite(curHorse.r, curHorse.c, nr, nc, curOrder);
			} else if (colorMap[nr][nc] == 1) {
				moveToRed(curHorse.r, curHorse.c, nr, nc, curOrder);
			} else {
				curHorse.dir = reverseDir(curHorse.dir);

				nr = curHorse.r + dr[curHorse.dir];
				nc = curHorse.c + dc[curHorse.dir];
				
				if (!checkBoundary(nr, nc)) {
					continue;
				}
				
				if (colorMap[nr][nc] == 0) {
					moveToWhite(curHorse.r, curHorse.c, nr, nc, curOrder);
				} else if (colorMap[nr][nc] == 1) {
					moveToRed(curHorse.r, curHorse.c, nr, nc, curOrder);
				}
				// 파란색인 경우에는 이동하지 않고 가만히 있는다.
			}
			
			if (horseMap[nr][nc].size() >= 4) {
				isFinished = true;
			}
			
			if (isFinished) return true;
		}
		
		return false;
	}
	
	private static void moveToRed(int r, int c, int nr, int nc, int curOrder) {
		int curSize = horseMap[r][c].size();
		int moveStartIdx = 0;
		
		for (int idx = 0; idx < curSize; idx++) {
			if (horseMap[r][c].get(idx).order == curOrder) {
				moveStartIdx = idx;
				break;
			}
		}
		
		stack.clear();
		
		Horse curHorse;
		// queue에 담기
		for (int idx = moveStartIdx; idx < curSize; idx++) {
			curHorse = horseMap[r][c].get(idx);
			curHorse.r = nr;
			curHorse.c = nc;
			stack.add(curHorse);
		}
		
		while (!stack.isEmpty()) {
			horseMap[nr][nc].add(stack.pop());
		}
		
		// 삭제
		for (int idx = curSize - 1; idx >= moveStartIdx; idx--) {
			horseMap[r][c].remove(idx);
		}
	}
	private static void moveToWhite(int r, int c, int nr, int nc, int curOrder) {
		
		int curSize = horseMap[r][c].size();
		int moveStartIdx = 0;
		
		for (int idx = 0; idx < curSize; idx++) {
			if (horseMap[r][c].get(idx).order == curOrder) {
				moveStartIdx = idx;
				break;
			}
		}
		
		Horse curHorse;
		for (int idx = moveStartIdx; idx < curSize; idx++) {
			curHorse = horseMap[r][c].get(idx);
			curHorse.r = nr;
			curHorse.c = nc;
			horseMap[nr][nc].add(curHorse);
		}
		
		// 삭제
		for (int idx = curSize - 1; idx >= moveStartIdx; idx--) {
			horseMap[r][c].remove(idx);
		}
		
	}
	
	private static int reverseDir(int dir) {
		if (dir == 1 || dir == 3) {
			return dir+1;
		} else {
			return dir-1;
		}
	}
	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=N && c>=1 && c<=N;
	}

}
