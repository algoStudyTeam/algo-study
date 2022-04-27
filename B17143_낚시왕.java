package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 10:35 - 
 *
 */
class Shark {
	int r;
	int c;
	int s;
	int d;
	int z;
	boolean alive;

	public Shark(int r, int c, int s, int d, int z, boolean alive) {
		super();
		this.r = r;
		this.c = c;
		this.s = s;
		this.d = d;
		this.z = z;
		this.alive = alive;
	}
}
public class BOJ_Gold2_17143_낚시왕 {
	
	static int R, C, M, map[][] = new int[105][105], sizeSum, curPersonC;
	static Shark sharks[] = new Shark[10010];
	static int dr[] = {0,-1,1,0,0};
	static int dc[] = {0,0,0,1,-1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		for (int r = 1; r <= R; r++) {
			Arrays.fill(map[r], 0);
		}
		
		for (int idx = 1; idx <= M; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int z = Integer.parseInt(st.nextToken());
			
			sharks[idx] = new Shark(r,c,s,d,z,true);
			
			map[r][c] = idx;
		}
		
		// process
		while (true) {
			// 1. 낚시왕 오른쪽 한칸 이동
			++curPersonC;
			
			if (curPersonC > C) break;
			
			// 2. 열에 있는 상어 중에 땅과 제일 가까운 상어 잡기
			getShark();
			
			// 3. 상어가 이동한다.
			moveSharks();
		}
		
		System.out.println(sizeSum);
	}

	private static void moveSharks() {
		for (int r = 1; r <= R; r++) {
			Arrays.fill(map[r], 0);
		}
		
		for (int idx = 1; idx <= M; idx++) {
			if (!sharks[idx].alive) continue;
			
			moveOneShark(idx);
		}
	}

	private static void moveOneShark(int idx) {
		Shark curShark = sharks[idx];
		int s = 0;
		
		if (curShark.d == 1 || curShark.d == 2) {
			s = curShark.s % ((R - 1) * 2);
		} else {
			s = curShark.s % ((C - 1) * 2);
		}
		
		int curR = curShark.r;
		int curC = curShark.c;
		int nr, nc;
		
		for (int dist = 1; dist <= s; dist++) {
			nr = curR + dr[curShark.d];
			nc = curC + dc[curShark.d];
			
			if (!checkBoundary(nr, nc)) {
				curShark.d = changeDir(curShark.d);
				
				nr = curR + dr[curShark.d];
				nc = curC + dc[curShark.d];
			}
			
			curR = nr;
			curC = nc;
		}
		
		curShark.r = curR;
		curShark.c = curC;
		
		if (map[curR][curC] != 0) {
			if (sharks[map[curR][curC]].z < curShark.z) {
				sharks[map[curR][curC]].alive = false;
				map[curR][curC] = idx;
			} else { // 이동한 상어가 작아서 잡아먹힘
				curShark.alive = false;
			}
		} else { // 아무것도 없으면 상어 놓기
			map[curR][curC] = idx;
		}
		
	}

	private static int changeDir(int d) {
		if (d==1 || d==3) {
			return ++d;
		} else {
			return --d;
		}
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=R && c>=1 && c<=C;
	}

	private static void getShark() {
		for (int r = 1; r <= R; r++) {
			if (map[r][curPersonC] != 0) {
				int idx = map[r][curPersonC];
				sizeSum += sharks[idx].z;
				sharks[idx].alive = false;
				
				map[r][curPersonC] = 0;
				break;
			}
		}
	}

}
