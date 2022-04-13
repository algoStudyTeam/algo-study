package algo220411_0417;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 4/13 2:40 - 
 * NxN 공간에 물고기 M마리, 아기상어 1마리
 * 한 칸에는 물고기 최대 1마리
 * 
 * 처음 아기 상어의 크기 = 2
 * -> 1초에 상하좌우로 인접한 한 칸씩 이동
 * 
 * 아기 상어 = 자신의 크기보다 큰 물고기가 있는 칸은 지나갈 수 없고, 나머지 칸은 모두 지나갈 수 있다.
 * 자신의 크기보다 작은 물고기만 먹을 수 있다.
 * 
 * 더 이상 먹을 수 있는 물고기가 공간에 없다면 아기 상어는 엄마 상어에게 도움을 요청한다.
먹을 수 있는 물고기가 1마리라면, 그 물고기를 먹으러 간다.
먹을 수 있는 물고기가 1마리보다 많다면, 거리가 가장 가까운 물고기를 먹으러 간다.
거리는 아기 상어가 있는 칸에서 물고기가 있는 칸으로 이동할 때, 지나야하는 칸의 개수의 최솟값이다.
거리가 가까운 물고기가 많다면, 가장 위에 있는 물고기, 그러한 물고기가 여러마리라면, 가장 왼쪽에 있는 물고기를 먹는다.
 * 
 * 이동 = 1초
 * 자신의 크기와 같은 수의 물고기를 먹을 때마다 크기가 1 증가
 * 
 * 몇초 동안 물고기를 잡아먹을 수 있는지?
 * 
 * visited 배열 안만들어서 무한루프...
 */
class Fish implements Comparable<Fish> {
	int r;
	int c;
	
	public Fish(int r, int c) {
		super();
		this.r = r;
		this.c = c;
	}

	@Override
	public int compareTo(Fish o) {
		if (this.r == o.r) {
			return this.c - o.c;
		} else {
			return this.r - o.r;
		}
	}
}
public class BOJ_Gold3_16236_아기상어 {

	static int map[][], N, time, sharkR, sharkC, sharkSize = 2, eatCnt, visited[][];
	static PriorityQueue<Fish> possibleFishes = new PriorityQueue<Fish>();
	static Queue<int[]> queue = new LinkedList<int[]>();
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		N = Integer.parseInt(in.readLine());
		
		map = new int[N][N];
		visited = new int[N][N];
		
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 0; c < N; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
				
				if (map[r][c] == 9) {
					sharkR = r;
					sharkC = c;
				}
			}
		}
		
		while (true) {
			
			if (!canEatFish()) {
				break;
			}
			
		}
		
		System.out.println(time);

	}
	
	static int dr[] = {1, -1, 0, 0};
	static int dc[] = {0, 0, 1, -1};
	private static boolean canEatFish() {
		queue.clear();
		possibleFishes.clear();
		
		for (int r = 0; r < N; r++) {
			Arrays.fill(visited[r], -1);
		}
		
		queue.offer(new int[] {sharkR, sharkC});
		visited[sharkR][sharkC] = 0;
		
		while (!queue.isEmpty()) {
			int qSize = queue.size();
			
			for (int i = 0; i < qSize; i++) {
				int[] curPos = queue.poll();
				
				for (int dir = 0; dir < 4; dir++) {
					int nr = curPos[0] + dr[dir];
					int nc = curPos[1] + dc[dir];
					
					// 공간 넘어가거나, 자신의 크기보다 큰 물고기가 있는 칸은 지나갈 수 없다.
					if (!checkBoundary(nr, nc) || map[nr][nc] > sharkSize || visited[nr][nc] != -1) continue;
					
					if (map[nr][nc] != 0 && map[nr][nc] < sharkSize) {
						possibleFishes.offer(new Fish(nr, nc));
					}
					queue.offer(new int[] {nr, nc});
					visited[nr][nc] = visited[curPos[0]][curPos[1]] + 1;
				}
			}
			
			if (possibleFishes.size() >= 1) {
				Fish eatFish = possibleFishes.poll();
				
				time += visited[eatFish.r][eatFish.c];
				map[sharkR][sharkC] = 0;
				map[eatFish.r][eatFish.c] = 9;
				sharkR = eatFish.r;
				sharkC = eatFish.c;
				++eatCnt;
				
				if (eatCnt == sharkSize) {
					++sharkSize;
					eatCnt = 0;
				}
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean checkBoundary(int r, int c) {
		return r>=0 && r<N && c>=0 && c<N;
	}

}
