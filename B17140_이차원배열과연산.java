package algo220418_0424;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 12:21 - 1:44 : 1시간 20분
 * 3x3 배열 A -> 인덱스 1부터 시작, 1초가 지날때마다 배열에 연산이 적용
 * 
 * R연산 : 배열 A의 모든 행에 대해서 정렬을 수행. 행개수 >= 열개수인 경우 적용
 * C연산 : 배열 A의 모든 열에 대해서 정렬을 수행. 행개수 < 열개수인 경우 적용
 * 
 * 정렬 - 수의 등장 횟수가 커지는 순으로, 그러한 것이 여러가지면 수가 커지는 순으로 정렬
 * 
 * 정렬된 결과를 배열에 넣을 때, 수와 등장 횟수를 모두 넣으며, 순서는 수가 먼저이다.
 *
 */
class NumbersNcnt implements Comparable<NumbersNcnt> {
	int number;
	int cnt;
	
	public NumbersNcnt(int number, int cnt) {
		super();
		this.number = number;
		this.cnt = cnt;
	}

	@Override
	public int compareTo(NumbersNcnt o) {
		if (this.cnt == o.cnt) {
			return this.number - o.number;
		} else {
			return this.cnt - o.cnt;
		}
	}
}

public class BOJ_Gold4_17140_이차원배열과연산 {

	static int A[][], time, r, c, k, rSize, cSize, rFinishIdx[], cFinishIdx[];
	static Map<Integer, Integer> numberMap = new HashMap<Integer, Integer>();
	static Set<Integer> numbersSet;
	static List<NumbersNcnt> numbersNcntList = new ArrayList<NumbersNcnt>();
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		A = new int[101][101];
		
		for (int r = 1; r <= 3; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 1; c <= 3; c++) {
				A[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		rSize = 3;
		cSize = 3;
		
		rFinishIdx = new int[101];
		cFinishIdx = new int[101];
		
		for (int idx = 1; idx <= 3; idx++) {
			rFinishIdx[idx] = 3;
			cFinishIdx[idx] = 3;
		}
		
		// process
		while (true) {
			if (A[r][c] == k) {
				System.out.println(time);
				return;
			}
			
			if (++time > 100) {
				break;
			}
			
			// R연산
			if (rSize >= cSize) {
				cSize = 0;
				for (int rIdx = 1; rIdx <= rSize; rIdx++) {
					checkRArray(rIdx);
				}
			} else { // C연산
				rSize = 0;
				for (int cIdx = 1; cIdx <= cSize; cIdx++) {
					checkCArray(cIdx);
				}
			}
		}
		
		System.out.println(-1);
		
	}
	
	private static void checkCArray(int cIdx) {
//		int curFinishRIdx = cFinishIdx[cIdx];
		
		numberMap.clear();
		numbersNcntList.clear();
		
		// 수 계산
		for (int rIdx = 1; rIdx <= 100; rIdx++) {
			if (A[rIdx][cIdx] == 0) continue;
			
			if (numberMap.containsKey(A[rIdx][cIdx])) {
				numberMap.put(A[rIdx][cIdx], numberMap.get(A[rIdx][cIdx]) + 1);
			} else {
				numberMap.put(A[rIdx][cIdx], 1);
			}
		}
		
		numbersSet = numberMap.keySet();
		Iterator<Integer> numbersIter = numbersSet.iterator();
		
		while (numbersIter.hasNext()) {
			int number = numbersIter.next();
			int cnt = numberMap.get(number);
			numbersNcntList.add(new NumbersNcnt(number, cnt));
		}
		
		Collections.sort(numbersNcntList);
		
		int nFinishRIdx = numbersNcntList.size() * 2;
		cFinishIdx[cIdx] = nFinishRIdx;

		rSize = Math.max(rSize, nFinishRIdx);

		for (int rIdx = 1; rIdx <= 100; rIdx++) { // 다 초기화 필요!!!!! 100까지!!
			A[rIdx][cIdx] = 0;
		}
		
		int rIdx = 1;
		for (NumbersNcnt numbersNcnt : numbersNcntList) {
			A[rIdx++][cIdx] = numbersNcnt.number;
			A[rIdx++][cIdx] = numbersNcnt.cnt;
			
			if (rIdx > 100) break;
		}
	}

	private static void checkRArray(int rIdx) {
//		int curFinishCIdx = rFinishIdx[rIdx];
		
		numberMap.clear();
		numbersNcntList.clear();
		
		// 수 계산
		for (int cIdx = 1; cIdx <= 100; cIdx++) {
			if (A[rIdx][cIdx] == 0) continue;
			
			if (numberMap.containsKey(A[rIdx][cIdx])) {
				numberMap.put(A[rIdx][cIdx], numberMap.get(A[rIdx][cIdx]) + 1);
			} else {
				numberMap.put(A[rIdx][cIdx], 1);
			}
		}
		
		numbersSet = numberMap.keySet();
		Iterator<Integer> numbersIter = numbersSet.iterator();
		
		while (numbersIter.hasNext()) {
			int number = numbersIter.next();
			int cnt = numberMap.get(number);
			numbersNcntList.add(new NumbersNcnt(number, cnt));
		}
		
		Collections.sort(numbersNcntList);
		
		int nFinishCIdx = numbersNcntList.size() * 2;
		rFinishIdx[rIdx] = nFinishCIdx;
		
		cSize = Math.max(cSize, nFinishCIdx);

		Arrays.fill(A[rIdx], 0);
		
		int cIdx = 1;
		for (NumbersNcnt numbersNcnt : numbersNcntList) {
			A[rIdx][cIdx++] = numbersNcnt.number;
			A[rIdx][cIdx++] = numbersNcnt.cnt;
			
			if (cIdx > 100) break;
		}
	}

}
