package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 20:30 - 10:36
 * 
 * 어항 - 정육면체 모양
 * 
 *
 */
public class BOJ_Platinum5_23291_어항정리 {

	static int map[][] = new int[101][101], rotateArr[][] = new int[101][101], tempArr[][] = new int[101][101], N, K, turnCnt;
	static int differ[][] = new int[101][101];
	static Queue<Integer> queue = new LinkedList<Integer>();
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		for (int r = 0; r < N; r++) {
			Arrays.fill(map[r], -1);
		}
		
		st = new StringTokenizer(in.readLine(), " ");
		for (int idx = 0; idx < N; idx++) {
			map[0][idx] = Integer.parseInt(st.nextToken());
		}
		
		while (true) {
			++turnCnt;
			
			// 0. 물고기 수가 가장 적은 어항에 물고기 넣기
			putMinFish();
			
			// 1. 어항 정리
			firstArrange();
			
			// 2. 어항 물고기 수 조절
			adjustFishes();
			
			// 3. 어항 일렬로 놓기
			makeLine();
			
			// 4. 공중부양 작업 (시계방향 180도)
			secondArrange();
			
			// 5. 어항 물고기 수 조절 
			adjustFishes();
			
			// 6. 어항 일렬로 놓기
			makeLine();
			
			if (checkFishCnt()) {
				break;
			}
		}

		System.out.println(turnCnt);
	}
	
	private static void putMinFish() {
		queue.clear();
		int minCnt = Integer.MAX_VALUE;
		for (int cIdx = 0; cIdx < N; cIdx++) {
			if (minCnt > map[0][cIdx]) {
				minCnt = map[0][cIdx];
				queue.clear();
				queue.offer(cIdx);
			} else if (minCnt == map[0][cIdx]) {
				queue.offer(cIdx);
			}
		}
		
		while (!queue.isEmpty()) {
			++map[0][queue.poll()];
		}
	}

	private static boolean checkFishCnt() {
		int maxCnt = Integer.MIN_VALUE;
		int minCnt = Integer.MAX_VALUE;
		
		for (int cIdx = 0; cIdx < N; cIdx++) {
			if (maxCnt < map[0][cIdx]) maxCnt = map[0][cIdx];
			if (minCnt > map[0][cIdx]) minCnt = map[0][cIdx];
		}
		
		if (maxCnt - minCnt <= K) return true;
		else return false;
	}

	private static void secondArrange() {
		// 첫번째
		int halfN = N/2;
		
		for (int cIdx = 0; cIdx < halfN; cIdx++) {
			rotateArr[0][halfN - 1 - cIdx] = map[0][cIdx];
			map[0][cIdx] = -1;
		}
		
		for (int cIdx = halfN; cIdx < N; cIdx++) {
			map[1][cIdx] = rotateArr[0][cIdx-halfN];
		}
		
		// 두번째
		int hhalfN = halfN / 2;
		
		for (int r = 0; r < 2; r++) {
			for (int c = halfN; c < halfN + hhalfN; c++) {
				tempArr[r][c-halfN] = map[r][c];
				map[r][c] = -1;
			}
		}
		
		// 180도 회전
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < hhalfN; c++) {
				rotateArr[r][c] = tempArr[2 - 1 - r][hhalfN - 1 - c];
			}
		}
		
		for (int r = 2; r < 4; r++) {
			for (int c = halfN + hhalfN; c < N; c++) {
				map[r][c] = rotateArr[r-2][c-halfN-hhalfN];
			}
		}
		
	}

	private static void makeLine() {
		queue.clear();
		
		// 큐에 담기
		for (int c = 0; c < N; c++) {
			for (int r = 0; r < N; r++) {
				if (map[r][c] == -1) continue;
				
				queue.offer(map[r][c]);
				map[r][c] = -1;
			}
		}
		
		// 맵에 넣기
		for (int cIdx = 0; cIdx < N; cIdx++) {
			map[0][cIdx] = queue.poll();
		}
		
	}

	static int dr[] = {-1,1,0,0};
	static int dc[] = {0,0,1,-1};
	private static void adjustFishes() {
		for (int r = 0; r < N; r++) {
			Arrays.fill(differ[r], 0);
		}
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (map[r][c] == -1) continue;
				
				for (int dir = 0; dir < 4; dir++) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];
					
					if (!checkBoundary(nr, nc) || map[nr][nc] == -1) continue;
					if (map[nr][nc] < map[r][c]) {
						int d = (map[r][c] - map[nr][nc]) / 5;
						
						differ[r][c] -= d;
						differ[nr][nc] += d;
					}
				}
			}
		}
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (map[r][c] == -1) continue;
				
				map[r][c] += differ[r][c];
			}
		}
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=0 && r<N && c>=0 && c<N;
	}

	private static void firstArrange() {
		int startC = 0, cLen = 1, rLen = 1, lenTurn = 0;
		
		while (true) {
			if (startC + cLen + rLen - 1 >= N) break; // 어항 바닥 없음
			
			// 회전할 배열 저장
			for (int r = 0; r < rLen; r++) {
				for (int c = startC; c < startC + cLen; c++) {
					tempArr[r][c-startC] = map[r][c];
					map[r][c] = -1;
				}
			}
			
			// 배열 시계방향 90도 회전
			for (int r = 0; r < cLen; r++) {
				for (int c = 0; c < rLen; c++) {
					rotateArr[r][c] = tempArr[c][cLen - 1 - r];
				}
			}
			
			// 회전한 배열 넣어주기
			for (int r = 1; r < cLen + 1; r++) {
				for (int c = startC + cLen; c < startC + cLen + rLen; c++) {
					map[r][c] = rotateArr[r-1][c-startC-cLen];
				}
			}
			
			startC += cLen;
			
			if (lenTurn % 2 == 0) {
				++rLen;
			} else {
				++cLen;
			}
			++lenTurn;
		}
		
	}

}
