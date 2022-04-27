package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 
 * 04/27 3:00 - 
 *
 */
public class BOJ_Gold5_21610_마법사상어와비바라기 {

	static int N, M, A[][] = new int[55][55], d[] = new int[105], s[] = new int[105];
	static boolean removeCloud[][] = new boolean[55][55];
	static List<int[]> clouds = new ArrayList<int[]>();
	static List<int[]> tempClouds = new ArrayList<int[]>();
	static int dr[] = {0,0,-1,-1,-1,0,1,1,1};
	static int dc[] = {0,-1,-1,0,1,1,1,0,-1};
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		init();
		
		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= N; c++) {
				A[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int m = 1; m <= M; m++) {
			st = new StringTokenizer(in.readLine(), " ");
			d[m] = Integer.parseInt(st.nextToken());
			s[m] = Integer.parseInt(st.nextToken());
		}
		
		clouds.add(new int[] {N,1});
		clouds.add(new int[] {N,2});
		clouds.add(new int[] {N-1,1});
		clouds.add(new int[] {N-1,2});
		
		// process
		for (int m = 1; m <= M; m++) {
			// 1. 모든 구름이 이동
			moveClouds(m);
			
			// 2. 각 구름에서 비가 내려 구름이 있는 칸의 바구니에 저장된 물의 양이 1 증가
			plusWater();
			
			// 3. 구름이 모두 사라진다.
			allRemoveCloud();
			
			// 4. 물복사버그 마법 시전 -> 여기서 clouds clear 하기
			duplicateWater();
			
			// 5. 바구니에 저장된 물의 양이 2 이상인 모든 칸에 구름이 생기고, 물의 양이 2 줄어든다. 이때 구름이 생기는 칸은 3에서 구름이 사라진 칸이 아니어야 한다.
			makeClouds();
		}
		
		System.out.println(sumWater());

	}
	
	private static int sumWater() {
		int sum = 0;
		
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				sum += A[r][c];
			}
		}
		
		return sum;
	}

	private static void makeClouds() {
		clouds.clear();
		
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (A[r][c] >= 2 && !removeCloud[r][c]) {
					clouds.add(new int[] {r, c});
					A[r][c] -= 2;
				}
			}
		}
		
	}

	static int xdr[] = {0,-1,-1,1,1};
	static int xdc[] = {0,-1,1,1,-1};
	private static void duplicateWater() {
		int cnt = 0;
		for (int[] curCloud : clouds) {
			cnt = 0;
			for (int dir = 1; dir <= 4; dir++) {
				int nr = curCloud[0] + xdr[dir];
				int nc = curCloud[1] + xdc[dir];
				
				if (!checkBoundary(nr, nc) || A[nr][nc] == 0) continue;
				
				++cnt;
			}
			
			A[curCloud[0]][curCloud[1]] += cnt;
		}
		
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=N && c>=1 && c<=N;
	}

	private static void allRemoveCloud() {
		for (int r = 1; r <= N; r++) {
			Arrays.fill(removeCloud[r], false);
		}
		
		for (int[] curCloud : clouds) {
			removeCloud[curCloud[0]][curCloud[1]] = true;
		}
		
//		clouds.clear();
	}

	private static void plusWater() {
		for (int[] curCloud : clouds) {
			++A[curCloud[0]][curCloud[1]];
		}
	}

	private static void moveClouds(int m) {
		tempClouds.clear();
		for (int[] curCloud : clouds) {
			int nr = curCloud[0] + s[m] * dr[d[m]];
			int nc = curCloud[1] + s[m] * dc[d[m]];
			
			nr = calcCurPos(nr);
			nc = calcCurPos(nc);
			
			tempClouds.add(new int[] {nr, nc});
		}
		
		clouds.clear();
		
		for (int[] cloud : tempClouds) {
			clouds.add(cloud);
		}
	}

	private static int calcCurPos(int pos) {
		if (pos > 0) {
			return pos % N != 0 ? pos % N : N;
		} else {
			return N + (pos % N);
		}
	}

	private static void init() {
		for (int r = 0; r < 55; r++) {
			Arrays.fill(A[r], 0);
			Arrays.fill(removeCloud[r], false);
		}
		
		for (int idx = 0; idx < 105; idx++) {
			Arrays.fill(d, 0);
			Arrays.fill(s, 0);
		}
		
		clouds.clear();
	}

}
