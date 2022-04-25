#include <iostream>
#include <algorithm>
using namespace std;
struct info {
	int x, y, dir;
	info(){}
	info(int x, int y, int dir) { this->x = x; this->y = y; this->dir = dir; }
};
int res;
int dx[9] = { 0,-1,-1,0,1,1,1,0,-1 }, dy[9] = { 0,0,-1,-1,-1,0,1,1,1 };
void moveFish(info fish2[], int arr2[][4], int val);
void moveShark(info fish2[], int arr2[][4], int val) {
	res = max(res, val);
	int arr[4][4] = { 0 };
	info fish[17];
	copy(&fish2[0], &fish2[0] + 17, &fish[0]);
	copy(&arr2[0][0], &arr2[0][0] + 4 * 4, &arr[0][0]);
	int xx = fish[0].x, yy = fish[0].y, dir = fish[0].dir;
	for (int i = 1; i <= 4; ++i) {
		int x = xx + i * dx[dir], y = yy + i * dy[dir];
		if (x < 0 || x >= 4 || y < 0 || y >= 4 || arr[x][y] == -1) continue;
		int num = arr[x][y];
		info tmp = fish[num];
		fish[0] = tmp;
		val += num;
		fish[num] = { -1,-1,-1 }; // 먹힌 물고기는 -1로 표시
		arr[x][y] = 0;
		arr[xx][yy] = -1; // 상어가 있던 자리는 빈 칸으로 표시
		moveFish(fish, arr, val);
		arr[xx][yy] = 0;
		arr[x][y] = num;
		fish[num] = tmp;
		val -= num;
		fish[0] = { xx,yy,dir };
	}
}
void moveFish(info fish2[], int arr2[][4], int val) {
	res = max(res, val);
	int arr[4][4] = { 0 };
	info fish[17];
	copy(&fish2[0], &fish2[0] + 17, &fish[0]);
	copy(&arr2[0][0], &arr2[0][0] + 4 * 4, &arr[0][0]);
	for (int i = 1; i <= 16; ++i) {
		if (fish[i].dir == -1) continue;
		for (int j = 0; j < 8; ++j) {
			int x = fish[i].x + dx[fish[i].dir], y = fish[i].y + dy[fish[i].dir];
			if (x < 0 || x >= 4 || y < 0 || y >= 4 || arr[x][y] == 0) { // 이동 못 하는 곳이면
				fish[i].dir = fish[i].dir % 8 + 1; // 방향 전환
				continue;
			}
			int xx = fish[i].x, yy = fish[i].y;
			if (arr[x][y] != -1) fish[arr[x][y]].x = xx, fish[arr[x][y]].y = yy;
			fish[i].x = x, fish[i].y = y;
			swap(arr[x][y], arr[xx][yy]);
			break;
		}
	}
	moveShark(fish, arr, val);
}
int main() {
	int arr[4][4] = { 0 };
	info fish[17];
	for (int i = 0; i < 4; ++i) {
		for (int j = 0; j < 4; ++j) {
			int a, b;
			cin >> a >> b;
			fish[a] = { i,j,b };
			arr[i][j] = a;
		}
	}
	res += arr[0][0];
	fish[0] = fish[arr[0][0]]; // 상어는 0번
	fish[arr[0][0]] = { -1,-1,-1 }; // 먹힌 물고기는 -1로 표시
	arr[0][0] = 0;
	moveFish(fish, arr, res);
	cout << res;
	return 0;
}