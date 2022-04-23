#include <iostream>
#include <algorithm>
#include <climits>
using namespace std;
int N, res = INT_MAX, tot;
int arr[22][22];

int main() {
	cin >> N;
	for (int i = 1; i <= N; ++i) for (int j = 1; j <= N; ++j) cin >> arr[i][j], tot += arr[i][j];
	for (int x = 1; x <= N; ++x) {
		for (int y = 1; y <= N; ++y) {

			for (int d1 = 1; d1 < N; ++d1) {
				for (int d2 = 1; d2 < N; ++d2) {
					if (x + d1 > N || y - d1 < 1 || x + d2 > N || y + d2 > N || x + d1 + d2 > N || y - d1 + d2 < 1 || y - d1 + d2 > N) continue; // 범위 벗어나면 구역 안 나눠짐
					int Min = INT_MAX, Max = INT_MIN, val = 0, fifth = tot;
					// 1번 구역
					for (int i = 1; i < x; ++i) for (int j = 1; j <= y; ++j) val += arr[i][j];
					for (int i = x; i < x + d1; ++i) for (int j = 1; j < y - i + x; ++j) val += arr[i][j];
					Min = min(Min, val), Max = max(Max, val);
					fifth -= val, val = 0;

					// 2번 구역
					for (int i = 1; i < x; ++i) for (int j = y + 1; j <= N; ++j) val += arr[i][j];
					for (int i = x; i <= x + d2; ++i) for (int j = y + i - x + 1; j <= N; ++j) val += arr[i][j];
					Min = min(Min, val), Max = max(Max, val);
					fifth -= val, val = 0;

					// 3번 구역
					for (int i = x + d1 + d2 + 1; i <= N; ++i) for (int j = 1; j < y - d1 + d2; ++j) val += arr[i][j];
					for (int i = x + d1; i <= x + d1 + d2; ++i) for (int j = 1; j < y - x + i - 2 * d1; ++j) val += arr[i][j];
					Min = min(Min, val), Max = max(Max, val);
					fifth -= val, val = 0;

					// 4번 구역
					for (int i = x + d1 + d2 + 1; i <= N; ++i) for (int j = y - d1 + d2; j <= N; ++j) val += arr[i][j];
					for (int i = x + d2 + 1; i <= x + d1 + d2; ++i) for (int j = y + x - i + 2 * d2+1; j <= N; ++j) val += arr[i][j];

					Min = min(Min, val), Max = max(Max, val);
					fifth -= val, val = 0;

					Min = min(Min, fifth), Max = max(Max, fifth);
					val = Max - Min;
					res = min(res, val);
				}
			}
		}
	}
	cout << res;
	return 0;
}