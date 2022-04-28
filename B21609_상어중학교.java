package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 8:42 - 9:41 + 15분 디버깅..
 *
 */
class BlockGroup implements Comparable<BlockGroup> {
	int totalCnt;
	int rainbowCnt;
	int centerR;
	int centerC;
	
	public BlockGroup(int totalCnt, int rainbowCnt, int centerR, int centerC) {
		super();
		this.totalCnt = totalCnt;
		this.rainbowCnt = rainbowCnt;
		this.centerR = centerR;
		this.centerC = centerC;
	}

	@Override
	public int compareTo(BlockGroup o) {
		if (this.totalCnt == o.totalCnt) {
			if (this.rainbowCnt == o.rainbowCnt) {
				if (this.centerR == o.centerR) {
					return o.centerC - this.centerC;
				}
				return o.centerR - this.centerR;
			}
			return o.rainbowCnt - this.rainbowCnt;
		}
		return o.totalCnt - this.totalCnt;
	}
}

public class BOJ_Gold2_21609_상어중학교 {

	static int map[][] = new int[22][22], tempMap[][] = new int[22][22], N, M, sumScore;
	static boolean visited[][] = new boolean[22][22];
	static int dr[] = {-1,1,0,0};
	static int dc[] = {0,0,1,-1};
	static PriorityQueue<BlockGroup> blockGroupsPQ = new PriorityQueue<BlockGroup>();
	static Queue<int[]> queue = new LinkedList<int[]>();
	static Queue<Integer> numQueue = new LinkedList<Integer>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		init();
		
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 0; c < N; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		while (true) {
			
			// 1. 가장 큰 블록그룹 찾기
			findBigBlockGroup();
			
			if (blockGroupsPQ.isEmpty()) {
				break;
			}
			
			// 2. 1에서 찾은 블록 그룹의 모든 블록 제거, 점수 획득
			removeBlock();
			
			// 3. 격자에 중력 작용
			fallBlock();
			
			// 4. 격자가 90도 반시계 회전
			rotate();
			
			// 5. 격자에 중력 작용
			fallBlock();
		}
		
		System.out.println(sumScore);
	}

	private static void rotate() {
		initTempMap();
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				tempMap[r][c] = map[c][N-1-r];
			}
		}
		
		copyMap();
	}

	private static void copyMap() {
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				map[r][c] = tempMap[r][c];
			}
		}
	}

	private static void initTempMap() {
		for (int r = 0; r < N; r++) {
			Arrays.fill(tempMap[r], 0);
		}
		
	}

	private static void fallBlock() {
		for (int c = 0; c < N; c++) {
			int curPutRIdx = N-1;
			numQueue.clear();
			
			for (int r = N-1; r >= 0; r--) {
				if (map[r][c] == -2) continue;
				else if (map[r][c] == -1) {
					while (!numQueue.isEmpty()) {
						map[curPutRIdx--][c] = numQueue.poll();
					}
					curPutRIdx = r-1;
				} else {
					numQueue.offer(map[r][c]);
					map[r][c] = -2;
				}
			}
			
			while (!numQueue.isEmpty()) {
				map[curPutRIdx--][c] = numQueue.poll();
			}
			curPutRIdx -= 1;
		}
		
	}

	private static void removeBlock() {
		BlockGroup curBlockGroup = blockGroupsPQ.poll();
		
		queue.clear();
		initVisited();
		
		int curColor = map[curBlockGroup.centerR][curBlockGroup.centerC];
		
		queue.offer(new int[] {curBlockGroup.centerR, curBlockGroup.centerC});
		visited[curBlockGroup.centerR][curBlockGroup.centerC] = true;
		map[curBlockGroup.centerR][curBlockGroup.centerC] = -2;
		
		while (!queue.isEmpty()) {
			int curR = queue.peek()[0];
			int curC = queue.peek()[1];
			queue.poll();
			
			for (int dir = 0; dir < 4; dir++) {
				int nr = curR + dr[dir];
				int nc = curC + dc[dir];
				
				// 검은색 블록 포함 안됨, 우지개 블록 갯수 상관 없음, 아무것도 없는 공간도 포함 안됨
				if (!checkBoundary(nr, nc) || visited[nr][nc] 
						|| map[nr][nc] == -1 || map[nr][nc] == -2) continue;
				
				if (map[nr][nc] == curColor || map[nr][nc] == 0) {
					queue.offer(new int[] {nr, nc});
					visited[nr][nc] = true;
					
					map[nr][nc] = -2;
				}
			}
		}
		
		sumScore += curBlockGroup.totalCnt * curBlockGroup.totalCnt;
	}

	private static void initVisited() {
		for (int i = 0; i < N; i++) {
			Arrays.fill(visited[i], false);
		}
	}

	private static void findBigBlockGroup() {
		int curColor;
		int cnt;
		int centerR;
		int centerC;
		int rainbowCnt;
		blockGroupsPQ.clear();
		initVisited();
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (map[r][c] == 0 || map[r][c] == -1 || map[r][c] == -2) continue;
				
				queue.clear();
				curColor = map[r][c];
				
				queue.offer(new int[] {r, c});
				visited[r][c] = true;
				cnt = 1;
				centerR = r;
				centerC = c;
				rainbowCnt = 0;
				
				while (!queue.isEmpty()) {
					int curR = queue.peek()[0];
					int curC = queue.peek()[1];
					queue.poll();
					
					for (int dir = 0; dir < 4; dir++) {
						int nr = curR + dr[dir];
						int nc = curC + dc[dir];
						
						// 검은색 블록 포함 안됨, 우지개 블록 갯수 상관 없음, 아무것도 없는 공간도 포함 안됨
						if (!checkBoundary(nr, nc) || visited[nr][nc] 
								|| map[nr][nc] == -1 || map[nr][nc] == -2) continue;
						
						if (map[nr][nc] == curColor || map[nr][nc] == 0) {
							++cnt;
							queue.offer(new int[] {nr, nc});
							visited[nr][nc] = true;
							
							if (map[nr][nc] == 0) { // 무지개 블록 카운팅
								++rainbowCnt;
							} else { // 기준블록 찾기
								if (centerR == nr) {
									if (centerC > nc) {
										centerR = nr;
										centerC = nc;
									}
								} else if (centerR > nr) {
									centerR = nr;
									centerC = nc;
								}
							}
							
						}
					}
				}
				
				// 블록의 개수는 2개보다 크거나 같아야 한다.
				if (cnt >= 2) {
					blockGroupsPQ.offer(new BlockGroup(cnt, rainbowCnt, centerR, centerC));
				}
				
				// 무지개 블록 소유권 돌려주기
				for (int tr = 0; tr < N; tr++) {
					for (int tc = 0; tc < N; tc++) {
						if (map[tr][tc] == 0) {
							visited[tr][tc] = false;
						}
					}
				}
			}
		}
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=0 && r<N && c>=0 && c<N;
	}

	private static void init() {
		for (int i = 0; i < N; i++) {
			Arrays.fill(map[i], 0);
			Arrays.fill(tempMap[i], 0);
			Arrays.fill(visited[i], false);
		}
		blockGroupsPQ.clear();
		
	}

}
