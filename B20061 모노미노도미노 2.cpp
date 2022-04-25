#include <iostream>
#include <algorithm>
#include <queue>
using namespace std;
int N, res, blockcnt;
int red[4][4], blue[4][6], green[6][4];
int whofirst(int x, int color) { // 블럭을 놓을 수 있는 위치 반환
	if (color == 0) { // green
		for (int i = 1; i < 6; ++i) {
			if (green[i][x] == 1) return i - 1;
		}
	}
	else {
		for (int i = 1; i < 6; ++i) {
			if (blue[x][i] == 1) return i - 1;
		}
	}
	return 5;
}
void findFullAndDestroy(int color) {
	if (color == 0) { // green
		for (int i = 0; i < 6; ++i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (green[i][j] == 1) ++cnt;
			if (cnt < 4) continue;
			for (int j = 0; j < 4; ++j) green[i][j] = 0;
			++res;
		}
	}
	else {
		for (int i = 0; i < 6; ++i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (blue[j][i] == 1) ++cnt;
			if (cnt < 4) continue;
			for (int j = 0; j < 4; ++j) blue[j][i] = 0;
			++res;
		}
	}
}
void fillEmpty(int color) {
	queue<int> tmp;
	if (color == 0) { // green
		for (int i = 5; i >= 0; --i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (green[i][j] == 1) ++cnt;
			if (cnt == 0) continue;
			for (int j = 0; j < 4; ++j) tmp.push(green[i][j]);
		}
		for (int i = 5; i >= 0; --i) {
			for (int j = 0; j < 4; ++j) {
				if (tmp.empty()) green[i][j] = 0;
				else {
					green[i][j] = tmp.front();
					tmp.pop();
				}
			}
		}
	}
	else {
		for (int i = 5; i >= 0; --i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (blue[j][i] == 1) ++cnt;
			if (cnt == 0) continue;
			for (int j = 0; j < 4; ++j) tmp.push(blue[j][i]);
		}
		for (int i = 5; i >= 0; --i) {
			for (int j = 0; j < 4; ++j) {
				if (tmp.empty()) blue[j][i] = 0;
				else {
					blue[j][i] = tmp.front();
					tmp.pop();
				}
			}
		}
	}
}
void findLightColor(int color) {
	int erase = 0;
	if (color == 0) { // green
		for (int i = 0; i < 2; ++i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (green[i][j] == 1) ++cnt;
			if (cnt > 0) ++erase;
		}
		for (int i = 5; i >= 6 - erase; --i) {
			for (int j = 0; j < 4; ++j) green[i][j] = 0;
		}
	}
	else {
		for (int i = 0; i < 2; ++i) {
			int cnt = 0;
			for (int j = 0; j < 4; ++j) if (blue[j][i] == 1) ++cnt;
			if (cnt > 0) ++erase;
		}
		for (int i = 5; i >= 6 - erase; --i) {
			for (int j = 0; j < 4; ++j) blue[j][i] = 0;
		}
	}
}
int main() {
	cin >> N;
	for (int n = 0; n < N; ++n) {
		int t, x, y, gx, gy, bx, by;
		cin >> t >> x >> y;
		if (t == 1) { // 1 x 1
			gx = whofirst(y, 0), gy = y;
			by = whofirst(x, 1), bx = x;
			green[gx][gy] = 1;
			blue[bx][by] = 1;
		}
		else if (t == 2) { // 1 x 2
			gx = min(whofirst(y, 0), whofirst(y + 1, 0)), gy = y;
			by = whofirst(x, 1) - 1, bx = x;
			green[gx][gy] = green[gx][gy + 1] = 1;
			blue[bx][by] = blue[bx][by + 1] = 1;
		}
		else { // 2 x 1
			gx = whofirst(y, 0) - 1, gy = y;
			by = min(whofirst(x, 1), whofirst(x + 1, 1)), bx = x;
			green[gx][gy] = green[gx + 1][gy] = 1;
			blue[bx][by] = blue[bx + 1][by] = 1;
		}
		findFullAndDestroy(0), findFullAndDestroy(1);
		fillEmpty(0), fillEmpty(1);
		findLightColor(0), findLightColor(1);
		fillEmpty(0), fillEmpty(1);
	}
	for (int i = 2; i < 6; ++i) {
		for (int j = 0; j < 4; ++j) {
			if (green[i][j] == 1) ++blockcnt;
			if (blue[j][i] == 1) ++blockcnt;
		}
	}
	cout << res << "\n" << blockcnt;
	return 0;
}