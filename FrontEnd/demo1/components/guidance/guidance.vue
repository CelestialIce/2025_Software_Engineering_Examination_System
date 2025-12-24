<template>
	<view class="navbar">
		<!-- 左侧 Logo 和导航链接 -->
		<view class="navbar-left">
			<image src="../../static/images/logo.png" alt="Logo" class="logo"></image>
			<navigator url="/pages/index/index">首页</navigator>
			<navigator url="/pages/online_test/online_test">在线考试</navigator>
			<!-- 管理员专用链接 -->
			<navigator v-if="isAdmin" url="/pages/manager/manager">题库管理</navigator>
			<navigator v-if="isAdmin" url="/pages/user_manage/user_manage">用户管理</navigator>
		</view>

		<!-- 右侧 登录/个人信息管理 -->
		<view class="navbar-right">
			<navigator v-if="!isLoggedIn" url="/pages/login/login">登录</navigator>
			<view v-else @click="goToUserProfile">个人信息管理</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "guidance",
		data() {
			return {
				isLoggedIn: false,
				isAdmin: false,
			};
		},
		created() {
			// 在应用启动时检查登录状态
			try {
				const token = localStorage.getItem('JWT');
				if (token) {
					// 解析JWT令牌，检查用户的userid状态
					const payload = JSON.parse(atob(token.split('.')[1]));
					this.isLoggedIn = true;
					this.isAdmin = payload.userid === 1000000; // 检查用户是否为管理员
				}
			} catch (e) {
				console.error('Error parsing token from localStorage: ', e);
			}
		},
		methods: {
			goToUserProfile() {
				// 跳转到用户个人信息页面
				this.$router.push('/pages/user/user');
			}
		}
	}
</script>

<style lang="scss">
	.navbar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: #333;
		padding: 10px 20px;
		color: white;
	}

	.navbar-left {
		display: flex;
		align-items: center;
	}

	.logo {
		height: 70rpx;
		width: 70rpx;
		margin-right: 20px;
	}

	.navbar-left navigator {
		margin: 0 15px;
		/* 调整间距 */
		color: white;
		text-decoration: none;
	}

	.navbar-right {
		display: flex;
		align-items: center;
		margin-right: 3%;
	}

	a {
		color: white;
		text-decoration: none;
		margin: 0 10px;
	}

	a:hover {
		text-decoration: underline;
	}
</style>