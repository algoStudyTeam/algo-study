package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 9:34 - 10:52 -> 1시간 18분 소요
 * 
 * NxN 격자에서 연습
 * 
 * N은 항상 홀수 (1,1)~(N,N)
 * 
 * 상어 -> ((N+1)/2, (N+1)/2)
 * 
 * 칸과 칸 사이에는 벽이 세워져 있다 => N=3,5,7인 경우
 * 
 * 처음에 상어가 있는 칸을 제외한 나머지 칸에는 구슬이 하나 들어갈 수 있다.
 * 
 * 같은 번호를 가진 구슬이 번호가 연속하는 칸에 있으면 => 연속구슬
 * 
 * 블리자드 마법
 * - 방향, 거리 -> 상어는 di 방향으로 거리가 si 이하인 모든 칸에 얼음 파편을 던져 그 칸에 있는 구슬을 모두 파괴
 * 	=> 그 칸은 구슬이 들어있지 않은 빈 칸이 된다
 * - 어떤 칸 A의 번호보다 번호가 하나 작은 칸이 빈 칸이면, A에 있는 구슬은 그 빈 칸으로 이동한다. 
 * 이 이동은 더 이상 구슬이 이동하지 않을 때까지 반복된다
 * 
 * - 폭발 -> 4개 이상 연속하는 구슬이 있을 때 발생
 * 	=> 폭발하고 나면 구슬 이동
 *  => 폭발이 없을 때까지 반복!
 *  
 * - 변화
 *  => 연속하는 구슬 = 하나의 그룹
 *  -> 두 개의 구슬 A, B 로 변함
 *  A : 그룹에 들어있는 구슬의 개수
 *  B : 그룹을 이루고 있는 구슬의 번호
 *  
 *  => 차례대로 칸에 넣는다.
 *  
 *  ** M번 시전 -> 1×(폭발한 1번 구슬의 개수) + 2×(폭발한 2번 구슬의 개수) + 3×(폭발한 3번 구슬의 개수)
 * 
 */
class Pos {
	int r;
	int c;
	
	public Pos(int r, int c) {
		super();
		this.r = r;
		this.c = c;
	}
}

public class BOJ_Gold1_21611_마법사상어와블리자드 {

	static int N, M, map[][], d[], s[], circleCnt[], sharkR, sharkC;
	static Pos order[];
	static List<Integer> circleQueue = new ArrayList<Integer>();
	static List<Integer> removeIdx = new ArrayList<Integer>();
	static Queue<Integer> changeCircleQueue = new LinkedList<Integer>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N+1][N+1];
		order = new Pos[N*N - 1];
		sharkR = sharkC = ((N+1)) / 2;
		circleCnt = new int[4];
		
		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= N; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		d = new int[M+1];
		s = new int[M+1];
		for (int idx = 1; idx <= M; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			d[idx] = Integer.parseInt(st.nextToken());
			s[idx] = Integer.parseInt(st.nextToken());
		}
		
		// 0. 셋팅 -> 순서대로 r,c 셋팅
		orderSetting();

		for (int m = 1; m <= M; m++) {
			// 1. 얼음파편 던지기
			throwIce(m);
			
			// 2. 폭발
			bombCircle();
			
			// 3. 변화
			changeCircle();
		}
		
		
		System.out.println(1*circleCnt[1] + 2*circleCnt[2] + 3*circleCnt[3]);
	}

	private static void changeCircle() {
		int cnt = 0;
		int curCircleNum = 0;
		
		int circleLen = circleQueue.size();
		changeCircleQueue.clear();
		
		for (int idx = 0; idx < circleLen; idx++) {
			
			if (circleQueue.get(idx) == 0) break;
			
			if (curCircleNum != circleQueue.get(idx)) {
				if (cnt != 0) {
					changeCircleQueue.offer(cnt);
					changeCircleQueue.offer(curCircleNum);
				}
				
				curCircleNum = circleQueue.get(idx);
				cnt = 1;
			} else {
				++cnt;
			}
		}
		
		if (cnt != 0) {
			changeCircleQueue.offer(cnt);
			changeCircleQueue.offer(curCircleNum);
		}
		
		for (Pos pos : order) {
			if (!changeCircleQueue.isEmpty()) {
				map[pos.r][pos.c] = changeCircleQueue.poll();
			} else {
				map[pos.r][pos.c] = 0;
			}
		}
		
	}

	private static void bombCircle() {
		
		while (true) {
			int cnt = 0;
			int startIdx = 0;
			int curIdx = 0;
			int curCircleNum = 0;
			boolean isBomb = false;
			
			int circleLen = circleQueue.size();
			removeIdx.clear();
			
			for (int idx = 0; idx < circleLen; idx++) {
				
				if (circleQueue.get(idx) == 0) break;
				
				if (curCircleNum != circleQueue.get(idx)) {
					if (cnt >= 4) {
						for (int tIdx = startIdx; tIdx <= curIdx; tIdx++) {
							++circleCnt[circleQueue.get(tIdx)];
							removeIdx.add(tIdx);
							isBomb = true;
						}
					}
					
					curCircleNum = circleQueue.get(idx);
					startIdx = idx;
					cnt = 1;
				} else {
					++cnt;
					curIdx = idx;
				}
			}
			
			if (cnt >= 4) {
				for (int tIdx = startIdx; tIdx <= curIdx; tIdx++) {
					++circleCnt[circleQueue.get(tIdx)];
					removeIdx.add(tIdx);
					isBomb = true;
				}
			}
			
			int removeIdxSize = removeIdx.size();
			for (int idx = removeIdxSize-1; idx >= 0; idx--) {
				circleQueue.remove((int) removeIdx.get(idx));
			}
			
			if (!isBomb) return;
			
		}
		
	}

	static int dr[] = {0,-1,1,0,0};
	static int dc[] = {0,0,0,-1,1};
	private static void throwIce(int m) {
		int curR = sharkR;
		int curC = sharkC;
		
		for (int plus = 1; plus <= s[m]; plus++) {
			curR += dr[d[m]];
			curC += dc[d[m]];
			
			map[curR][curC] = 0;
		}
		
		circleQueue.clear();
		
		// 구슬 큐에 담기
		for (Pos curPos : order) {
			if (map[curPos.r][curPos.c] != 0) {
				circleQueue.add(map[curPos.r][curPos.c]);
				map[curPos.r][curPos.c] = 0;
			}
		}
	}

	static int tdr[] = {0, 0, 1, 0, -1};
	static int tdc[] = {0, -1, 0, 1, 0};
	private static void orderSetting() {
		int plus = 1;
		int goCnt = 0;
		
		int curR = sharkR;
		int curC = sharkC;
		int curDir = 1;
		
		int idx = 0;
		while (true) {
			for (int i = 1; i <= plus; i++) {
				curR += tdr[curDir];
				curC += tdc[curDir];
				
				order[idx++] = new Pos(curR, curC);
				
				if (idx >= N*N - 1) return;
			}
			
			curDir = (curDir + 1) > 4 ? (curDir + 1) % 4 : (curDir + 1);
			++goCnt;
			
			if (goCnt == 2) {
				goCnt = 0;
				++plus;
			}
		}
	
	}

}
