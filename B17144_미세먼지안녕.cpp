#include <iostream>
using namespace std;
int R, C, T, res;
int arr[51][51], spread[51][51], xy[2] = { -1,-1 };
int dx[4] = { -1,1,0,0 }, dy[4] = { 0,0,-1,1 };
int main() {
	cin >> R >> C >> T;
	for (int i = 0; i < R; ++i) {
		for (int j = 0; j < C; ++j) {
			cin >> arr[i][j];
			if (arr[i][j] == -1 && xy[0] == -1) xy[0] = i, xy[1] = j;
		}
	}
	for (int t = 0; t < T; ++t) {
		// 1. 먼지 확산
		for (int i = 0; i < R; ++i) {
			for (int j = 0; j < C; ++j) {
				if (arr[i][j] > 0) {
					int cnt = 0, val = arr[i][j] / 5;
					for (int k = 0; k < 4; ++k) {
						int x = i + dx[k], y = j + dy[k];
						if (x < 0 || x >= R || y < 0 || y >= C || arr[x][y] == -1) continue;
						spread[x][y] += val; // 확산한 먼지는 따로 저장
						++cnt;
					}
					arr[i][j] -= val * cnt; // 확산 후 남은 먼지
				}
			}
		}
		for (int i = 0; i < R; ++i) { // 먼지 더하기
			for (int j = 0; j < C; ++j) {
				if (arr[i][j] == -1) continue;
				arr[i][j] += spread[i][j];
				spread[i][j] = 0;
			}
		}

		// 2. 공기 청정기 작동
		// 위쪽 공청
		int x = xy[0], y = xy[1], val = 0;
		for (int j = 1; j < C; ++j) swap(val, arr[x][j]);
		for (int i = x - 1; i >= 0; --i) swap(val, arr[i][C - 1]);
		for (int j = C - 2; j >= 0; --j) swap(val, arr[0][j]);
		for (int i = 1; i < x; ++i) swap(val, arr[i][0]);
		// 아래쪽 공청
		++x, val = 0;
		for (int j = 1; j < C; ++j) swap(val, arr[x][j]);
		for (int i = x + 1; i < R; ++i) swap(val, arr[i][C - 1]);
		for (int j = C - 2; j >= 0; --j) swap(val, arr[R - 1][j]);
		for (int i = R - 2; i > x; --i) swap(val, arr[i][0]);
	}
	// 3. 남은 먼지 양 출력
	for (int i = 0; i < R; ++i) for (int j = 0; j < C; ++j) if (arr[i][j] > 0) res += arr[i][j];
	cout << res;
	return 0;
}