package algo220418_0424;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 10:40 - 11:54 -> 1시간 14분
 * 
 * NxM 지도
 * 오른쪽 동쪽, 위는북쪽
 * (1,1) - (N,M)
 * 
 * 주사위 - (1,1)에 놓여져 있다.
 * 지도의 각 칸에도 정수가 하나씩 있다.
 * 
 * 가장 처음에 주사위의 이동방향 ->
 * 
 * 주사위가 이동 방향으로 한 칸 굴러간다. 만약, 이동 방향에 칸이 없다면, 이동 방향을 반대로 한 다음 한 칸 굴러간다.
주사위가 도착한 칸 (x, y)에 대한 점수를 획득한다.
주사위의 아랫면에 있는 정수 A와 주사위가 있는 칸 (x, y)에 있는 정수 B를 비교해 이동 방향을 결정한다.
A > B인 경우 이동 방향을 90도 시계 방향으로 회전시킨다.
A < B인 경우 이동 방향을 90도 반시계 방향으로 회전시킨다.
A = B인 경우 이동 방향에 변화는 없다.
 *
 */
public class BOJ_Gold3_23288_주사위굴리기2 {

	static int map[][], scoreSum, curB, diceR, diceC, diceDir, diceGaro[], diceSero[], N, M, K;
	static boolean visited[][];
	static Queue<int[]> queue = new LinkedList<int[]>();
	static int dr[] = {0, 1, 0, -1};
	static int dc[] = {1, 0, -1, 0};
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		map = new int[N+1][M+1];
		visited = new boolean[N+1][M+1];
		
		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= M; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		diceR = 1;
		diceC = 1;
		diceDir = 0;
		diceGaro = new int[] {1,3,6,4};
		diceSero = new int[] {1,5,6,2};
		
		for (int k = 0; k < K; k++) {
			// 0. 방향 체크, 현재위치 갱신
			checkDir();
			// 1. 이동
			moveDice();
			
			// 2. bfs -> 점수 구하기
			getScore();
			
			// 3. 방향 갱신
			updateDir();
		}
		
		System.out.println(scoreSum);
	}
	
	private static void getScore() {
		curB = map[diceR][diceC];
		
		for (int r = 1; r <= N; r++) {
			Arrays.fill(visited[r], false);
		}
		
		queue.clear();
		
		queue.offer(new int[] {diceR, diceC});
		visited[diceR][diceC] = true;
		int count = 1;
		
		while (!queue.isEmpty()) {
			int curR = queue.peek()[0];
			int curC = queue.peek()[1];
			queue.poll();
			
			for (int dir = 0; dir < 4; dir++) {
				int nr = curR + dr[dir];
				int nc = curC + dc[dir];
				
				if (!checkBoundary(nr, nc) || visited[nr][nc] || curB != map[nr][nc]) continue;
				
				queue.offer(new int[] {nr, nc});
				visited[nr][nc] = true;
				++count;
			}
		}
		
		scoreSum += count * curB;
	}

	private static void checkDir() {
		int nr = diceR + dr[diceDir];
		int nc = diceC + dc[diceDir];
		
		if (!checkBoundary(nr, nc)) {
			diceDir = (diceDir + 2) % 4;
			
			nr = diceR + dr[diceDir];
			nc = diceC + dc[diceDir];
		}
		
		diceR = nr;
		diceC = nc;
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=N && c>=1 && c<=M;
	}

	private static void updateDir() {
		
		// 방향 갱신
		int curA = diceGaro[2];
		
		if (curA > curB) {
			diceDir = (diceDir + 1) % 4;
		} else if (curA < curB) {
			diceDir = (diceDir - 1) < 0 ? 4 + (diceDir - 1) : diceDir - 1;
		}
	}

	private static void moveDice() {
		int temp;
		if (diceDir == 0) { // 오른쪽
			// 가로 오른쪽으로 밀기
			temp = diceGaro[3];
			for (int idx = 2; idx >= 0 ; idx--) {
				diceGaro[idx+1] = diceGaro[idx];
			}
			diceGaro[0] = temp;
			
			diceSero[0] = diceGaro[0];
			diceSero[2] = diceGaro[2];
		} else if (diceDir == 1) { // 아래
			// 세로 아래쪽으로 밀기
			temp = diceSero[3];
			for (int idx = 2; idx >= 0 ; idx--) {
				diceSero[idx+1] = diceSero[idx];
			}
			diceSero[0] = temp;

			diceGaro[0] = diceSero[0];
			diceGaro[2] = diceSero[2];
		} else if (diceDir == 2) { // 왼
			// 가로 왼쪽으로 밀기
			temp = diceGaro[0];
			for (int idx = 1; idx <= 3; idx++) {
				diceGaro[idx - 1] = diceGaro[idx];
			}
			diceGaro[3] = temp;

			diceSero[0] = diceGaro[0];
			diceSero[2] = diceGaro[2];

		} else { // 위
			// 세로 위쪽으로 밀기
			temp = diceSero[0];
			for (int idx = 1; idx <= 3; idx++) {
				diceSero[idx - 1] = diceSero[idx];
			}
			diceSero[3] = temp;

			diceGaro[0] = diceSero[0];
			diceGaro[2] = diceSero[2];

		}

	}

}
