<template>
	<view class="exampaper">
		<view class="pic">
			<image :src="examImage" mode="aspectFill"></image>
		</view>
		<view class="text">
			<view class="title">
				<view class="t">
					{{ examTitle }}
				</view>
			</view>
			<view class="info">
				<text>发布者：{{ examiner }}</text>
				<text>{{ examDate }}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "exampaper",
		data() {
			return {
				examTitle: '',
				examiner: '',
				examDate: '',
				examImage: '../../static/images/背景4.jpg' // 默认图片路径
			};
		},
		mounted() {
			this.fetchExamDetails();
		},
		methods: {
			fetchExamDetails() {
				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						url: 'http://localhost:8080//Test/paperExistCheck',
						method: 'GET',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json'
						},
						success: (res) => {
							if (res.data.success === 1) {
								const exam = res.data.data;
								this.examTitle = exam.title;
								this.examiner = exam.examiner;
								this.examDate = exam.date;
								if (exam.image) {
									this.examImage = exam.image;
								}
							} else {
								console.error('获取考试详情失败:', res.data.message);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			}
		}
	};
</script>

<style lang="scss">
	.exampaper {
		height: 240px;
		width: 400px;
		border: 1px solid #ffffff;
		display: flex;

		.pic {
			height: 240px;
			width: 200px;

			image {
				width: 100%;
				height: 100%;
			}
		}

		.text {
			border: 1px solid #ccc;
			flex: 1;
			padding-left: 20px;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.title {
				font-size: 24px;
				color: #333;
			}

			.t {
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
				overflow: hidden;
				text-overflow: ellipsis;
			}

			.info {
				font-size: 18px;
				color: #999;

				text {
					padding-top: 10px;
					padding-right: 30px;
				}
			}
		}
	}
</style>