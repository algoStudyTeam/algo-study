#include <iostream>
#include <queue>
#include <vector>
#include <algorithm>
using namespace std;
int M, S, idx, res;
int sdx[5] = { 0,-1,0,1,0 }, sdy[5] = { 0,0,-1,0,1 }, dx[9] = { 0,0,-1,-1,-1,0,1,1,1 }, dy[9] = { 0,-1,-1,0,1,1,1,0,-1 };
vector<int> arr[5][5], tmp[5][5], movetmp, movedfish[5][5];
vector<pair<int, int> > removedfish;
int shark[2], smell[5][5], sharkmove[65][3], sharkmoveto[3];

void permutation(int cnt) {
	if (cnt == 3) {
		sharkmove[idx][0] = movetmp[0];
		sharkmove[idx][1] = movetmp[1];
		sharkmove[idx][2] = movetmp[2];
		++idx;
		return;
	}
	for (int i = 1; i <= 4; ++i) {
		movetmp.push_back(i);
		permutation(cnt + 1);
		movetmp.pop_back();
	}
}

int main() {
	cin >> M >> S;
	for (int m = 0; m < M; ++m) {
		int a, b, c;
		cin >> a >> b >> c;
		arr[a][b].push_back(c);
	}
	cin >> shark[0] >> shark[1];
	permutation(0); // 상어 이동 경우의 수 64가지 미리 구해놓음

	for (int s = 0; s < S; ++s) {
		// 1. 물고기 복제
		copy(&arr[0][0], &arr[0][0] + 5 * 5, &tmp[0][0]);

		// 2. 모든 물고기 이동
		for (int i = 1; i <= 4; ++i) {
			for (int j = 1; j <= 4; ++j) {
				for (int k = 0; k < arr[i][j].size(); ++k) {
					bool chk = false;
					for (int m = 0; m < 8; ++m) {
						int x = i + dx[arr[i][j][k]], y = j + dy[arr[i][j][k]];
						if (x < 1 || x>4 || y < 1 || y>4 || (x == shark[0] && y == shark[1]) || smell[x][y] > 0) {
							arr[i][j][k] += 6, arr[i][j][k] %= 8, ++arr[i][j][k];
							continue;
						}
						chk = true;
						movedfish[x][y].push_back(arr[i][j][k]);
						break;
					}
					if (!chk) movedfish[i][j].push_back(arr[i][j][k]);
				}
			}
		}
		for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) arr[i][j].clear();
		copy(&movedfish[0][0], &movedfish[0][0] + 5 * 5, &arr[0][0]);
		for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) movedfish[i][j].clear();

		// 3. 상어 이동
		sharkmoveto[0] = -1, sharkmoveto[1] = -1, sharkmoveto[2] = -1;
		for (int i = 0; i < 65; ++i) {
			bool chk = false;
			int cnt = 0, x = shark[0], y = shark[1];
			vector<pair<int, int> > candidate;
			bool visited[5][5] = { false };
			for (int j = 0; j < 3; ++j) {
				x += sdx[sharkmove[i][j]], y += sdy[sharkmove[i][j]];
				if (x < 1 || x>4 || y < 1 || y>4) {
					chk = true;
					break;
				}
				if (visited[x][y]) continue;
				cnt += arr[x][y].size();
				candidate.push_back({ x,y });
				visited[x][y] = true;
			}
			if (!chk && cnt > sharkmoveto[2]) {
				sharkmoveto[0] = x, sharkmoveto[1] = y, sharkmoveto[2] = cnt;
				removedfish.clear();
				while (!candidate.empty()) removedfish.push_back(candidate.back()), candidate.pop_back();
			}
		}
		shark[0] = sharkmoveto[0], shark[1] = sharkmoveto[1];

		// 4. 물고기 냄새 관리
		for (int i = 1; i <= 4; ++i) {
			for (int j = 1; j <= 4; ++j) {
				if (smell[i][j] == 0) continue;
				++smell[i][j];
				if (smell[i][j] == 3) smell[i][j] = 0;
			}
		}
		while (!removedfish.empty()) {
			int x = removedfish.back().first, y = removedfish.back().second;
			if (arr[x][y].size() > 0) {
				smell[x][y] = 1;
				arr[x][y].clear();
			}
			removedfish.pop_back();
		}
	
		// 5. 물고기 복제한 거 완료
		for (int i = 1; i <= 4; ++i) {
			for (int j = 1; j <= 4; ++j) {
				for (int k = 0; k < tmp[i][j].size(); ++k) {
					arr[i][j].push_back(tmp[i][j][k]);
				}
			}
		}
	}
	for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) res += arr[i][j].size();
	cout << res;
	return 0;
}