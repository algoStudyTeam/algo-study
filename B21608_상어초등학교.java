package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 10:03 - 10:43 -> 40분
 *
 */
class Student implements Comparable<Student> {
	int r;
	int c;
	int likeCnt;
	int blankCnt;
	
	public Student(int r, int c, int likeCnt, int blankCnt) {
		super();
		this.r = r;
		this.c = c;
		this.likeCnt = likeCnt;
		this.blankCnt = blankCnt;
	}

	@Override
	public int compareTo(Student o) {
		if (this.likeCnt == o.likeCnt) {
			if (this.blankCnt == o.blankCnt) {
				if (this.r == o.r) {
					return this.c - o.c;
				}
				return this.c - o.c;
			}
			return o.blankCnt - this.blankCnt;
		}
		return o.likeCnt - this.likeCnt;
	}
	
	
}
public class BOJ_Silver1_21608_상어초등학교 {
	
	static int N, order[] = new int[405], map[][] = new int[22][22], sumScore;
	static Set<Integer> studentLike[] = new HashSet[405];
	static PriorityQueue<Student> likeQueue = new PriorityQueue<Student>();
	static int dr[] = {-1,1,0,0};
	static int dc[] = {0,0,-1,1};

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(in.readLine());
		
		for (int idx = 1; idx <= N*N; idx++) {
			studentLike[idx] = new HashSet<Integer>();
		}
		
		StringTokenizer st = null;
		
		for (int idx = 1; idx <= N*N; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			int studentNum = Integer.parseInt(st.nextToken());
			
			order[idx] = studentNum;
			studentLike[studentNum].add(Integer.parseInt(st.nextToken()));
			studentLike[studentNum].add(Integer.parseInt(st.nextToken()));
			studentLike[studentNum].add(Integer.parseInt(st.nextToken()));
			studentLike[studentNum].add(Integer.parseInt(st.nextToken()));
		}
		
		// process
		// 좋아하는 학생이 인접한 칸에 가장 많은 칸
		for (int idx = 1; idx <= N*N; idx++) {
			findLikeManyPersonPos(idx);
			
			Student curStudent = likeQueue.poll();
			
			map[curStudent.r][curStudent.c] = order[idx];
		}
		
		getScore();
		
		System.out.println(sumScore);
	}

	private static void getScore() {
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				int cnt = 0;
				for (int dir = 0; dir < 4; dir++) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];
					
					if (studentLike[map[r][c]].contains(map[nr][nc])) ++cnt;
				}
				
				if (cnt == 1) sumScore += 1;
				else if (cnt == 2) sumScore += 10;
				else if (cnt == 3) sumScore += 100;
				else if (cnt == 4) sumScore += 1000;
			}
		}
		
	}

	private static void findLikeManyPersonPos(int idx) {
		int curStudentNum = order[idx];
		int maxLikeCnt = -1;
		likeQueue.clear();

		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (map[r][c] != 0)
					continue;

				int likeCnt = 0;
				int blankCnt = 0;
				for (int dir = 0; dir < 4; dir++) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];

					if (!checkBoundary(nr, nc))
						continue;

					if (map[nr][nc] == 0) {
						++blankCnt;
					} else if (studentLike[curStudentNum].contains(map[nr][nc])) {
						++likeCnt;
					}
				}

				if (likeCnt > maxLikeCnt) {
					maxLikeCnt = likeCnt;

					likeQueue.clear();
					likeQueue.offer(new Student(r, c, likeCnt, blankCnt));
				} else if (likeCnt == maxLikeCnt) {
					likeQueue.offer(new Student(r, c, likeCnt, blankCnt));
				}
			}
		}

	}

	private static boolean checkBoundary(int r, int c) {
		return r>=1 && r<=N && c>=1 && c<=N;
	}

}
