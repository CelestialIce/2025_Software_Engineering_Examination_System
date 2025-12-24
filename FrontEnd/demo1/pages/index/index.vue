<template>
	<scroll-view scroll-y="true" class="container">
		<!-- 导航栏 -->
		<guidance></guidance>
		<!-- 欢迎使用在线考试系统！ -->
		<view class="main-wrapper">
			<view class="box">
				<text>欢迎使用在线考试系统！</text>
			</view>
			<!-- 轮播图 -->
			<view class="uni-margin-wrap">
				<swiper class="swiper" circular autoplay interval="5000">
					<swiper-item>
						<image src="../../static/images/背景1.jpg" mode="aspectFill"></image>
					</swiper-item>
					<swiper-item>
						<image src="../../static/images/背景2.jpg" mode="aspectFill"></image>
					</swiper-item>
					<swiper-item>
						<image src="../../static/images/背景3.jpg" mode="aspectFill"></image>
					</swiper-item>
				</swiper>
			</view>
			<!-- 公告栏和团队成员介绍 -->
			<view class="main-content">
				<!-- 左侧公告栏 -->
				<view class="announcement-section">
					<view class="section-title">
						公告栏
						<button v-if="isAdmin" @click="addAnnouncement()">添加公告</button>
					</view>
					<view class="announcement" v-for="(announcement, index) in announcements" :key="index">
						<text>{{ announcement.text }}</text><br>
						<text>发布时间: {{ announcement.date }}</text>
						<button v-if="isAdmin" @click="editAnnouncement(index)">编辑</button>
						<button v-if="isAdmin" @click="deleteAnnouncement(index)">删除</button>
					</view>
				</view>
				<!-- 右侧时间和团队成员 -->
				<view class="right-content">
					<!-- 当前时间 -->
					<view class="time-section">
						<view class="section-title">当前时间</view>
						<view class="time">
							<text>{{ currentTime }}</text>
						</view>
					</view>
					<!-- 团队成员 -->
					<view class="team-section">
						<view class="section-title">团队成员</view>
						<view class="team">
							<view class="team-member" v-for="member in teamMembers" :key="member.name">
								<image :src="member.image" mode="aspectFill" class="team-image"></image>
								<text>{{ member.name }}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 公告编辑弹窗 -->
		<view v-if="showAnnouncementModal" class="modal-overlay" @click.self="closeAnnouncementModal()">
			<view class="modal">
				<view class="modal-header">
					<h2>{{ editIndex === -1 ? '添加公告' : '编辑公告' }}</h2>
					<button class="close-button" @click="closeAnnouncementModal()">&times;</button>
				</view>
				<form @submit.prevent="saveAnnouncement">
					<view class="input-group">
						<label for="announcement-text">公告内容</label>
						<textarea id="announcement-text" v-model="announcementText" placeholder="请输入公告内容"
							required></textarea>
					</view>
					<view class="button-group">
						<button type="submit">{{ editIndex === -1 ? '添加' : '保存' }}</button>
					</view>
				</form>
			</view>
		</view>
	</scroll-view>
</template>

<script>
	import guidance from '../../components/guidance/guidance.vue';

	export default {
		name: 'Home',
		components: {
			guidance
		},
		data() {
			return {
				currentTime: '',
				teamMembers: [{
						name: '陈靖辉',
						image: '../../static/images/member1.jpg'
					},
					{
						name: '贺新铠',
						image: '../../static/images/member2.jpg'
					},
					{
						name: '赵子墨',
						image: '../../static/images/member3.jpg'
					},
					{
						name: '金哲吉',
						image: '../../static/images/member4.jpg'
					},
					{
						name: '邢延起',
						image: '../../static/images/member5.jpg'
					},
				],
				announcements: [{
						text: '公告1：系统升级通知',
						date: '2023-07-01'
					},
					{
						text: '公告2：期末考试安排',
						date: '2023-06-25'
					},
					{
						text: '公告3：假期安排',
						date: '2023-06-20'
					}
				],
				isAdmin: false,
				showAnnouncementModal: false,
				announcementText: '',
				editIndex: -1,
			};
		},
		created() {
			this.updateTime();
			setInterval(this.updateTime, 1000);
			this.checkAdminStatus();
		},
		methods: {
			updateTime() {
				const now = new Date();
				this.currentTime = now.toLocaleString();
			},
			checkAdminStatus() {
				try {
					const token = localStorage.getItem('JWT');
					if (token) {
						const payload = JSON.parse(atob(token.split('.')[1]));
						this.isAdmin = payload.userid === 1000000;
					}
				} catch (e) {
					console.error('Error parsing token from localStorage: ', e);
				}
			},
			addAnnouncement() {
				this.announcementText = '';
				this.editIndex = -1;
				this.showAnnouncementModal = true;
			},
			editAnnouncement(index) {
				this.announcementText = this.announcements[index].text;
				this.editIndex = index;
				this.showAnnouncementModal = true;
			},
			deleteAnnouncement(index) {
				this.announcements.splice(index, 1);
			},
			saveAnnouncement() {
				const date = new Date().toISOString().split('T')[0];
				if (this.editIndex === -1) {
					this.announcements.push({
						text: this.announcementText,
						date
					});
				} else {
					this.announcements[this.editIndex].text = this.announcementText;
					this.announcements[this.editIndex].date = date;
				}
				this.closeAnnouncementModal();
			},
			closeAnnouncementModal() {
				this.showAnnouncementModal = false;
			}
		}
	};
</script>

<style lang="scss">
	.container {
		display: flex;
		justify-content: center;
		/* 水平居中 */
		align-items: cover;
		/* 垂直居中 */
		padding: 10px;
		background-image: url(../../static/images/首页背景.png);
		background-size: cover;
		/* 保持图片原始大小 */
		background-position: center center;
		width: 100vw;
		height: 100vh;
		overflow-y: auto;
	}

	.main-wrapper {
		max-width: 1200px;
		/* 设置最大宽度 */
		width: auto;
		/* 固定宽度 */
		height: auto;
		align-self: center;
		background-repeat: repeat;
		/* 重复背景图片 */
		background-image: url(../../static/images/restaurant.webp);
		background-size: contain;
		background-position: center center;
		border-radius: 20px;
		padding: 20px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
		margin-top: 40rpx;
		margin-left: auto;
		margin-right: auto;
		opacity: 0.8;
		/* 设置透明度 */
	}

	.swiper {
		height: 400px;
		border-radius: 20px;
		overflow: hidden;
		margin-bottom: 20px;
	}

	.swiper image {
		width: 100%;
		height: 100%;
	}

	.box {
		display: flex;
		justify-content: center;
		padding: 30px;
		font-size: 40px;
		font-weight: bold;
		color: #185ABD;
	}

	.main-content {
		display: flex;
		justify-content: space-between;
		padding: 20px;
	}

	.announcement-section,
	.time-section,
	.team-section {
		background-color: rgba(255, 255, 255, 0.9);
		border-radius: 20px;
		padding: 20px;
		margin: 10px;
		flex: 1;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	}

	.announcement-section {
		margin-right: 10px;
	}

	.right-content {
		display: flex;
		flex-direction: column;
		flex: 1;
	}

	.section-title {
		font-size: 30px;
		font-weight: bold;
		color: #333;
		margin-bottom: 10px;
	}

	.announcement,
	.time,
	.team {
		padding: 10px;
		word-wrap: break-word;
		/* 确保文本自动换行 */
	}

	.team {
		display: flex;
		flex-wrap: wrap;
	}

	.team-member {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin: 10px;
	}

	.team-image {
		width: 100px;
		height: 100px;
		border-radius: 50%;
		margin-bottom: 10px;
	}

	/* 导航栏样式 */
	.navbar {
		display: flex;
		justify-content: space-between;
		/* 两端对齐 */
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

	.navbar-right {
		display: flex;
		align-items: center;
	}

	.avatar {
		height: 40px;
		width: 40px;
		border-radius: 50%;
		cursor: pointer;
	}

	a {
		color: white;
		text-decoration: none;
		margin: 0 10px;
	}

	a:hover {
		text-decoration: underline;
	}

	/* 公告编辑弹窗样式 */
	.modal-overlay {
		position: fixed;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 1000;
	}

	.modal {
		position: relative;
		background: #fff;
		padding: 20px;
		width: 400px;
		border-radius: 10px;
		box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
		z-index: 1001;
	}

	.modal-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px solid #eee;
		padding-bottom: 10px;
		margin-bottom: 20px;
	}

	.close-button {
		background: none;
		border: none;
		font-size: 24px;
		cursor: pointer;
		color: #999;
	}

	.close-button:hover {
		color: #333;
	}

	.input-group {
		display: flex;
		flex-direction: column;
		margin-bottom: 20px;
	}

	.input-group label {
		margin-bottom: 5px;
		font-weight: bold;
	}

	.input-group textarea {
		width: 95%;
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 5px;
		resize: none;
	}

	.button-group {
		display: flex;
		justify-content: center;
	}

	.button-group button {
		padding: 10px 20px;
		border: none;
		border-radius: 5px;
		background-color: #3498db;
		color: #fff;
		font-size: 16px;
		cursor: pointer;
	}

	.button-group button:hover {
		background-color: #2980b9;
	}
</style>
