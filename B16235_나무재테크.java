package algo220411_0417;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 22.04.11 11:44 - 
 * 
 * NxN -> 1x1 크기의 칸 
 * r,c는 1부터 시작
 * 
 * 가장 처음 모든 칸에 양분은 5만큼 들어있다.
 * 
 * M개의 나무를 구매해 땅에 심음
 * 1x1크기의 칸에 여러 개의 나무가 심어져 있을 수도.
 * 
 * 봄
 * 자신의 나이만큼 양분을 먹고, 나이가 1 증가 (자신의 칸에 있는 양분만 먹음)
 * 여러 개의 나무가 있다면, 나이가 어린 나무부터 양분을 먹는다. (자신의 나이만큼 양분을 먹을 수 없는 나무는 먹지 못하고 즉시 죽는다.)
 * 
 * 여름
 * 봄에 죽은 나무가 양분으로 변하게 된다.
 * => 각각의 죽은 나무마다 나이를 2로 나눈 값이 나무가 있던 칸에 양분으로 추가. (소수점 아래는 버린다.)
 * 
 * 가을
 * 나무가 번식 (나이가 5의 배수인 나무만)
 * => 인접한 8개의 칸에 나이가 1인 나무가 생긴다.
 * 
 * 겨울
 * S2D2가 땅을 돌아다니면서 땅에 양분 추가 => A[r][c] 추가
 * 
 * ** K년이 지난 후, 상도의 땅에 살아있는 나무의 개수
 * 
 * <총평>
 * - 조회가 많으면 LinkedList 말고 ArrayList 사용해야 시간이 줄어듬!!!!
 *
 */

public class BOJ_Gold4_16235_나무재테크 {

	static int N, M, K, A[][], ground[][], deadTree[][], newTrees[][];
	static List<Integer> trees[][];
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		A = new int[N][N];
		ground = new int[N][N];
		deadTree = new int[N][N];
		trees = new LinkedList[N][N];
		newTrees = new int[N][N];
		
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(in.readLine());
			for (int c = 0; c < N; c++) {
				A[r][c] = Integer.parseInt(st.nextToken());
				trees[r][c] = new LinkedList<Integer>();
				ground[r][c] = 5;
			}
		}
		
		int r,c,age;
		for (int idx = 0; idx < M; idx++) {
			st = new StringTokenizer(in.readLine());
			
			r = Integer.parseInt(st.nextToken()) - 1;
			c = Integer.parseInt(st.nextToken()) - 1;
			age = Integer.parseInt(st.nextToken());
			
			trees[r][c].add(age);
		}
		
		for (int year = 0; year < K; year++) {
			spring();
			summer();
			autumn();
			winter();
		}
		
		System.out.println(countTrees());
	}
	
	private static int countTrees() {
		int cnt = 0;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				cnt += trees[r][c].size();
			}
		}
		
		return cnt;
	}

	private static void winter() {
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				ground[r][c] += A[r][c];
			}
		}
	}

	static int dr[] = {-1,-1,-1,0,0,1,1,1};
	static int dc[] = {-1,0,1,-1,1,-1,0,1};
	private static void autumn() {
		int size;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				size = trees[r][c].size();
				
				for (int idx = 0; idx < size; idx++) {
					if (trees[r][c].get(idx) % 5 == 0) {
						
						for (int dir = 0; dir < 8; dir++) {
							int nr = r + dr[dir];
							int nc = c + dc[dir];
							
							if (!checkBoundary(nr, nc)) continue;
							
							++newTrees[nr][nc];
						}
					}
				}
			}
		}
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				
				if (newTrees[r][c] != 0) {
					for (int i = 0; i < newTrees[r][c]; i++) {
						trees[r][c].add(1);
					}
					newTrees[r][c] = 0;
				}
			}
		}
		
	}

	private static boolean checkBoundary(int r, int c) {
		return r>=0 && r<N && c>=0 && c<N;
	}

	private static void summer() {
//		 * 봄에 죽은 나무가 양분으로 변하게 된다.
//		 * => 각각의 죽은 나무마다 나이를 2로 나눈 값이 나무가 있던 칸에 양분으로 추가. (소수점 아래는 버린다.)
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				ground[r][c] += deadTree[r][c];
				deadTree[r][c] = 0;
			}
		}
	}

	private static void spring() {
//		 * 자신의 나이만큼 양분을 먹고, 나이가 1 증가 (자신의 칸에 있는 양분만 먹음)
//		 * 여러 개의 나무가 있다면, 나이가 어린 나무부터 양분을 먹는다. (자신의 나이만큼 양분을 먹을 수 없는 나무는 먹지 못하고 즉시 죽는다.)
		int size;
		int checkIdx;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (trees[r][c].size() >= 2) {
					checkIdx = -1;
					Collections.sort(trees[r][c]);
					
					size = trees[r][c].size();
					for (int idx = 0; idx < size; idx++) {
						if (trees[r][c].get(idx) > ground[r][c]) {
							checkIdx = idx;
							break;
						} else {
							ground[r][c] -= trees[r][c].get(idx);
							trees[r][c].set(idx, trees[r][c].get(idx) + 1);
						}
					}
					
					if (checkIdx != -1) {
						for (int idx = size - 1; idx >= checkIdx; idx--) {
							deadTree[r][c] += trees[r][c].get(idx) / 2;
							trees[r][c].remove(idx);
						}
					}
				} else if (trees[r][c].size() == 1) {
					if (trees[r][c].get(0) > ground[r][c]) {
						deadTree[r][c] += trees[r][c].get(0) / 2;
						trees[r][c].remove(0);
					} else {
						ground[r][c] -= trees[r][c].get(0);
						trees[r][c].set(0, trees[r][c].get(0) + 1);
					}
				}
			}
		}
		
//		 * 
	}
}
