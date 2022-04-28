package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 10:25 - 11:17 42분 소요
 *
 */
class Fireball {
	int r;
	int c;
	int m;
	int d;
	int s;
	
	public Fireball(int r, int c, int m, int d, int s) {
		super();
		this.r = r;
		this.c = c;
		this.m = m;
		this.d = d;
		this.s = s;
	}
}

public class BOJ_Gold4_20056_마법사상어와파이어볼 {
	
	static int N, M, K;
	static ArrayList<Fireball> map[][] = new ArrayList[55][55];
	static ArrayList<Fireball> tempMap[][] = new ArrayList[55][55];
	static int dr[] = {-1,-1,0,1,1,1,0,-1};
	static int dc[] = {0,1,1,1,0,-1,-1,-1};

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				map[r][c] = new ArrayList<Fireball>();
				tempMap[r][c] = new ArrayList<Fireball>();
			}
		}
		
		for (int idx = 1; idx <= M; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			map[r][c].add(new Fireball(r, c, m, d, s));
		}
		
		// process
		for (int k = 1; k <= K; k++) {
			
			// 1. 모든 파이어볼 이동
			moveFireballs();
			
			// 2. 파이어볼이 2개 이상 있는 칸에서 일어나는 일
			seperateFireballs();
		}
		
		System.out.println(remainSumM());
		
	}

	private static int remainSumM() {
		int sumM = 0;
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				for (Fireball curFireball : map[r][c]) {
					sumM += curFireball.m;
				}
			}
		}
		
		return sumM;
	}

	private static void seperateFireballs() {
		initMap();
		
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (tempMap[r][c].size() == 0) {
					continue;
				} else if (tempMap[r][c].size() == 1) {
					map[r][c].add(tempMap[r][c].get(0));
					continue;
				}
				
				int fireballCnt = tempMap[r][c].size();
				int sumM = 0;
				int sumS = 0;
				int oddD = 0;
				int evenD = 0;
				
				for (Fireball curFireball : tempMap[r][c]) {
					sumM += curFireball.m;
					sumS += curFireball.s;
					
					if (curFireball.d % 2 == 0) {
						++evenD;
					} else {
						++oddD;
					}
				}
				
				int seperateM = sumM/5;
				int seperateS = sumS/fireballCnt;
				
				// 질량이 0인 파이어볼은 소멸되어 없어진다.
				if (seperateM == 0) {
					continue;
				}
				
				if (oddD == 0 || evenD == 0) {
					// 모두 홀수거나 모두 짝수면 방향은 0,2,4,6
					for (int dir = 0; dir <= 6; dir+=2) {
						map[r][c].add(new Fireball(r, c, seperateM, dir, seperateS));
					}
				} else {
					// 방향 1,3,5,7
					for (int dir = 1; dir <= 7; dir+=2) {
						map[r][c].add(new Fireball(r, c, seperateM, dir, seperateS));
					}
				}
			}
		}
		
		initTempMap();
	}

	private static void moveFireballs() {
		initTempMap();
		
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (map[r][c].size() == 0) continue;
				
				int size = map[r][c].size();
				
				for (int idx = size-1; idx >= 0; idx--) {
					Fireball curFireball = map[r][c].get(idx);
					
					int nr = curFireball.r + curFireball.s*dr[curFireball.d];
					int nc = curFireball.c + curFireball.s*dc[curFireball.d];
					
					nr = minimumPos(nr);
					nc = minimumPos(nc);
					
					curFireball.r = nr;
					curFireball.c = nc;
					tempMap[nr][nc].add(curFireball);
					
					map[r][c].remove((int) idx);
				}
			}
		}
		
//		initMap();
//		for (int r = 1; r <= N; r++) {
//			for (int c = 1; c <= N; c++) {
//				map[r][c] = (ArrayList<Fireball>) tempMap[r][c].clone();
//			}
//		}
	}

	private static void initMap() {
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				map[r][c].clear();
			}
		}
	}

	private static int minimumPos(int pos) {
		if (pos >= 0) {
			return pos%N == 0 ? N : pos%N;
		} else {
			return pos%N == 0 ? N : N + (pos%N);
		}
	}

	private static void initTempMap() {
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				tempMap[r][c].clear();
			}
		}
	}

}
