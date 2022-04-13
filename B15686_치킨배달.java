package algo220321_0327;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 11:14 -
 * NxN
 * 1x1
 * 
 * 빈 칸, 치킨집, 집
 * r,c는 1부터 시작
 * 
 * 치킨거리 - 집과 가장 가까운 치킨집 사이의 거리
 * 치킨 거리 = 집을 기준으로 정해진다.
 * 
 * 도시의 치킨 거리 = 모든 집의 치킨 거리의 합
 * 
 * 두칸 (R1, c1)과 (r2, c2) 사이의 거리 = |r1-r2| + |c1-c2|
 * 
 * 0 빈칸
 * 1 집
 * 2 치킨집
 * 
 * 일부 치킨집을 폐업 => 가장 수익을 많이 낼 수 있는 치킨집의 개수는 최대 M개
 * 
 * 최대 M개의 치킨집을 고르고, 나머지 치킨집은 모두 폐업 -> 도시의 치킨거리가 가장 작게 될지 구하기
 * 
 * 2<=N<=50
 * 1<=M<=13
 * 
 * 집의 개수는 2N개를 넘지 않으며, 적어도 1개는 존재!
 * 치킨집 개수 -> M보다 크거나 같고, 13보다 작거나 같다
 * 
 * 
 */
public class BOJ_Gold5_15686_치킨배달 {

	static int map[][], N, M, cityChickenDist = Integer.MAX_VALUE, comb[], homeSize, chickenSize;
	static List<int[]> home, chicken;
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N+1][N+1];
		home = new LinkedList<int[]>();
		chicken = new LinkedList<int[]>();
		comb = new int[M];
		
		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(in.readLine());
			for (int c = 1; c <= N; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
				
				if (map[r][c] == 1) home.add(new int[] {r,c});
				else if (map[r][c] == 2) chicken.add(new int[] {r,c});
			}
		}
		
		homeSize = home.size();
		chickenSize = chicken.size();
		
		// process
		selectChicken(0, 0);
		
		System.out.println(cityChickenDist);
		
	}
	
	private static void selectChicken(int idx, int start) {
		if (idx == M) {
			findCityChickenDist();
			return;
		}
		
		for (int i = start; i < chickenSize; i++) {
			comb[idx] = i;
			selectChicken(idx+1, i+1);
		}
		
	}

	private static void findCityChickenDist() {
		
		int curHome[], curChicken[], curChickenDist, curDist, distSum = 0;
		
		for (int hIdx = 0; hIdx < homeSize; hIdx++) {
			curHome = home.get(hIdx);
			curChickenDist = Integer.MAX_VALUE;
			
			for (int combIdx = 0; combIdx < M; combIdx++) {
				curChicken = chicken.get(comb[combIdx]);
				
				curDist = Math.abs(curHome[0] - curChicken[0]) + Math.abs(curHome[1] - curChicken[1]);
				curChickenDist = Math.min(curChickenDist, curDist);
			}
			
			distSum += curChickenDist;
		}
		
		cityChickenDist = Math.min(cityChickenDist, distSum);
	}
}
