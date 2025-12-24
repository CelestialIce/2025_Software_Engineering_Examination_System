<template>
	<scroll-view scroll-y="true" class="container">
		<!-- 导航栏 -->
		<guidance></guidance>
		<problem></problem>
		<!-- 题库信息 -->
		<view class="on-item">
			<view v-for="(paper, index) in examPapers" :key="index" class="exampaper"
				:class="{ selected: selectedPaperIndex === index }" @click="selectPaper(index)">
				<view>{{ paper.title }}</view>
				<view>{{ paper.author }}</view>
				<view>{{ paper.date }}</view>
			</view>
		</view>
		<!-- 专业选择 -->
		<view class="select">
			<picker mode="multiSelector" :range="multiArry" @change="bindMultiPickChange" :value="multiIndex">
				<view class="uni-input">
					<text>请选择你的专业：</text>
					{{ multiArry[0][multiIndex[0]] }}、{{ multiArry[1][multiIndex[1]] }}
				</view>
			</picker>
			<view class="start">
				<button @click="startExam()" class="start-button">开始考试</button>
			</view>
		</view>
	</scroll-view>
</template>

<script>
	import {
		mapState
	} from 'vuex';

	export default {
		data() {
			return {
				multiArry: [
					['计算机科学与技术', '软件工程', '人工智能'],
					['通信工程', '数字媒体技术', '网络安全']
				],
				multiIndex: [0, 0],
				examPapers: [],
				selectedPaperIndex: null
			};
		},
		computed: {
			...mapState(['userId']) // 从 Vuex 获取用户ID
		},
		created() {
			this.paperExistCheck();
		},
		methods: {
			paperExistCheck() {
				uni.request({
					url: 'http://localhost:8080/Test/paperExistCheck',
					method: 'POST',
					header:{
						"JWT":localStorage.getItem("JWT")
					},
					data:{
						"userid":parseInt(localStorage.getItem("userId"))
					},
					success: (res) => {
						if (res.data.success === 1) {
							this.examPapers = res.data.data;
							uni.navigateTo({
								url: `/pages/test_page/test_page`
							});
						}
					},
					fail: (err) => {
						uni.showToast({
							title: '请求失败',
							icon: 'none',
							duration: 2000
						});
						console.error(err);
					}
				});
			},
			bindMultiPickChange(e) {
				this.multiIndex = e.detail.value;
			},
			startExam() {
				const selectedMajor = `${this.multiArry[0][this.multiIndex[0]]}/${this.multiArry[1][this.multiIndex[1]]}`;
				uni.request({
					url: 'http://localhost:8080/Test/testCreate',
					method: 'POST',
					header:{
						"JWT":localStorage.getItem("JWT")
					},
					data: {
						userid: localStorage.getItem("userId"), // 使用实际用户ID
						major: selectedMajor
					},
					success: (res) => {
						if (res.data.success === 1) {
							this.examPapers = res.data.data;
							uni.showToast({
								title: '试卷生成成功',
								icon: 'success',
								duration: 2000
							});
							// 跳转到考试页面
							uni.navigateTo({
								url: '/pages/test_page/test_page'
							});
						} else {
							uni.showToast({
								title: '试卷生成失败',
								icon: 'none',
								duration: 2000
							});
						}
					},
					fail: (err) => {
						uni.showToast({
							title: '请求失败',
							icon: 'none',
							duration: 2000
						});
						console.error(err);
					}
				});
			}
		}
	};
</script>

<style lang="scss">
	.container {
		padding: 20px;
		background-image: url('../../static/images/restaurant.webp');
		background-size: cover;
		background-position: center;
		width: 100vw;
		height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.on-item {
		padding: 20px;
		display: flex;
		justify-content: space-around;
		flex-wrap: wrap;
	}

	.exampaper {
		width: 45%;
		margin: 10px;
		border: 1px solid #ccc;
		border-radius: 10px;
		overflow: hidden;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		cursor: pointer;
	}

	.exampaper.selected {
		border: 2px solid #3498db;
	}

	.select {
		padding: 20px;
		font-size: 20px;
		background-color: rgba(255, 255, 255, 0.8);
		border-radius: 10px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		width: 80%;
		text-align: center;
	}

	.uni-input {
		margin: 10px 0;
		padding: 10px;
		font-size: 18px;
		color: #333;
		border: 1px solid #ccc;
		border-radius: 10px;
		background-color: #fff;
	}

	.start {
		margin-top: 20px;
	}

	.start-button {
		width: 50%;
		height: 50px;
		border: none;
		border-radius: 10px;
		background-color: #3498db;
		color: #fff;
		font-size: 18px;
		cursor: pointer;
	}

	.start-button:hover {
		background-color: #2980b9;
	}
</style>