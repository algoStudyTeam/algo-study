#include <iostream>
#include <algorithm>
#include <climits>
#include <vector>
#include <cstdlib>
#include <cstring>
using namespace std;
int N, K, res, Min, Max, R, C;
int aqua[102], arr[51][51];
int dx[4] = { 0,-1,0,1 }, dy[4] = { -1,0,1,0 }; // 우상좌하
int tmp[51][51] = { 0 }, visited[51][51][51][51], halffold[4] = {3,0,1,2};
vector<int> foldorder;
int checkDiff() {
	Min = INT_MAX, Max = INT_MIN;
	for (int i = 1; i <= N; ++i) Min = min(Min, aqua[i]), Max = max(Max, aqua[i]);
	return Max - Min;
}
void controlFish(int row, int col) {
	memset(visited, 0, sizeof(visited));
	memset(tmp, 0, sizeof(tmp));
	copy(&arr[0][0], &arr[0][0] + 51 * 51, &tmp[0][0]);
	for (int i = 0; i < row; ++i) {
		for (int j = 0; j < col; ++j) {
			if (arr[i][j] == 0) continue;
			for (int k = 0; k < 4; ++k) {
				int x = i + dx[k], y = j + dy[k];
				if (x < 0 || x >= row || y < 0 || y >= col || visited[i][j][x][y] || arr[x][y] == 0)continue;
				visited[x][y][i][j] = visited[i][j][x][y] = 1;
				int d = abs(arr[i][j] - arr[x][y]) / 5;
				if (d > 0) {
					if (arr[i][j] > arr[x][y]) tmp[i][j] -= d, tmp[x][y] += d;
					else tmp[i][j] += d, tmp[x][y] -= d;
				}
			}
		}
	}
	copy(&tmp[0][0], &tmp[0][0] + 51 * 51, &arr[0][0]);
}
int main() {
	cin >> N >> K;
	for (int i = 1; i <= N; ++i) cin >> aqua[i];
	int val = 0;
	for (int i = 1; i <= N; ++i) {
		for (int j = 0; j < 2; ++j) {
			if (j == 0 && N - (val + i) > 0 && N - (val + i) < i) foldorder.push_back(N - val), val = N;
			else if (j == 1 && N - (val + i) > 0 && N - (val + i) < i + 1) foldorder.push_back(N - val), val = N;
			else foldorder.push_back(i), val += i;
			if (val == N) break;
		}
		if (val == N) break;
	}
	R = foldorder[foldorder.size() - 2] + 1, C = foldorder[foldorder.size() - 1];

	while (1) {
		if (checkDiff() <= K) break;
		// 1. 물고기 수 가장 적은 어항 조절
		for (int i = 1; i <= N; ++i) if (aqua[i] == Min) ++aqua[i];
		
		// 2. 어항 접기
		int r = R - 1, c = C, d = 0, idx = N;
		for (int k = foldorder.size() - 1; k >= 0; --k) {
			for (int i = 0; i < foldorder[k]; ++i) {
				r += dx[d], c += dy[d];
				arr[r][c] = aqua[idx--];
			}
			d = (d + 1) % 4;
		}

		// 3. 물고기 수 조절
		controlFish(R, C);

		// 4. 어항 바닥에 일렬로 놓기
		idx = 1;
		for (int j = 0; j < C; ++j) {
			for (int i = R - 1; i >= 0; --i) {
				if (arr[i][j] == 0) continue;
				aqua[idx++] = arr[i][j];
			}
		}
		memset(arr, 0, sizeof(arr));

		// 5. 절반으로 두 번 접기
		idx = N;
		for (int i = 0; i < 4; ++i) {
			r = halffold[i];
			if (i % 2)for (int c = 0; c < N / 4; ++c) arr[r][c] = aqua[idx--];
			else for (int c = N / 4 - 1; c >= 0; --c) arr[r][c] = aqua[idx--];
		}

		// 6. 물고기 수 조절
		controlFish(4, N/4);

		// 7. 어항 바닥에 일렬로 놓기
		idx = 1;
		for (int j = 0; j < N/4; ++j) {
			for (int i = 3; i >= 0; --i) {
				if (arr[i][j] == 0) continue;
				aqua[idx++] = arr[i][j];
			}
		}
		memset(arr, 0, sizeof(arr));

		++res;
	}

	cout << res;
	return 0;
}