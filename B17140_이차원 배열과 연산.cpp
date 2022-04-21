#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;
int r, c, K, M = 3, N = 3;
int arr[101][101];
void R() {
	vector <pair<int, int>> v[101];
	int Max = 1;
	for (int i = 0; i < M; i++) {
		int tmp[101] = { 0 };
		for (int j = 0; j < N; j++) {
			if (arr[i][j]) {
				if (!tmp[arr[i][j]]) v[i].push_back(make_pair(1, arr[i][j])), tmp[arr[i][j]] = v[i].size();
				else v[i][tmp[arr[i][j]] - 1].first++;
			}
		}
		sort(v[i].begin(), v[i].end());
		if (v[i].size() * 2 > 100) Max = 100;
		else Max = max(Max, (int)v[i].size() * 2);
	}
	N = Max;
	for (int i = 0; i < M; i++) {
		int tmp = 0;
		for (int j = 0; j < N; j++) {
			if (j >= v[i].size() * 2) arr[i][j] = 0;
			else arr[i][j] = v[i][tmp].second, arr[i][++j] = v[i][tmp++].first;
		}
	}
}
void C() {
	vector <pair<int, int>> v[101];
	int Max = 1;
	for (int i = 0; i < N; i++) {
		int tmp[101] = { 0 };
		for (int j = 0; j < M; j++) {
			if (arr[j][i]) {
				if (!tmp[arr[j][i]]) v[i].push_back(make_pair(1, arr[j][i])), tmp[arr[j][i]] = v[i].size();
				else v[i][tmp[arr[j][i]] - 1].first++;
			}
		}
		sort(v[i].begin(), v[i].end());
		if (v[i].size() * 2 > 100) Max = 100;
		else Max = max(Max, (int)v[i].size() * 2);
	}
	M = Max;
	for (int i = 0; i < N; i++) {
		int tmp = 0;
		for (int j = 0; j < M; j++) {
			if (j >= v[i].size() * 2) arr[j][i] = 0;
			else arr[j][i] = v[i][tmp].second, arr[++j][i] = v[i][tmp++].first;
		}
	}
}
int main() {
	cin >> r >> c >> K;
	for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) cin >> arr[i][j];
	int cnt = 0;
	while (1) {
		if (arr[r - 1][c - 1] == K || cnt > 100) break;
		if (M >= N) R();
		else C();
		cnt++;
	}
	if (cnt > 100) cout << -1;
	else cout << cnt;
	return 0;
}