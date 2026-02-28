<template>
	<view class="faq-page">
		<!-- 导航栏 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">{{ $t('faq.title') }}</text>
			<view class="placeholder"></view>
		</view>
		
		<!-- 常见问题列表 -->
		<view class="faq-list">
			<view class="faq-item" v-for="(item, index) in faqList" :key="index">
				<view class="faq-question" @click="toggleAnswer(index)">
					<text class="question-text">{{ item.question }}</text>
					<view class="arrow" :class="{ 'arrow-up': item.expanded }"></view>
				</view>
				<view class="faq-answer" :class="{ 'expanded': item.expanded }">
					<text class="answer-text">{{ item.answer }}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'FAQPage',
		data() {
			return {
				faqList: [
					{
						question: this.$t('faq.questions.q1'),
						answer: this.$t('faq.questions.a1'),
						expanded: false
					},
					{
						question: this.$t('faq.questions.q2'),
						answer: this.$t('faq.questions.a2'),
						expanded: false
					},
					{
						question: this.$t('faq.questions.q3'),
						answer: this.$t('faq.questions.a3'),
						expanded: false
					},
					{
						question: this.$t('faq.questions.q4'),
						answer: this.$t('faq.questions.a4'),
						expanded: false
					},
					{
						question: this.$t('faq.questions.q5'),
						answer: this.$t('faq.questions.a5'),
						expanded: false
					}
				]
			}
		},
		mounted() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			toggleAnswer(index) {
				// 先关闭所有展开的项
				this.faqList.forEach((item, i) => {
					if (i !== index) {
						item.expanded = false;
					}
				});
				
				// 切换当前项的展开状态
				this.faqList[index].expanded = !this.faqList[index].expanded;
			}
		}
	}
</script>

<style>
	.faq-page {
		min-height: 100vh;
		background-color: var(--color-bg, #f5f5f5);
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 常见问题列表 */
	.faq-list {
		flex: 1;
		padding: 24rpx 32rpx;
	}
	
	.faq-item {
		background-color: var(--color-card, #FFFFFF);
		border-radius: 16rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
		overflow: hidden;
	}
	
	.faq-question {
		padding: 32rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		cursor: pointer;
	}
	
	.question-text {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		flex: 1;
	}
	
	.arrow {
		width: 0;
		height: 0;
		border-left: 8rpx solid transparent;
		border-right: 8rpx solid transparent;
		border-top: 12rpx solid #999999;
		transition: transform 0.3s ease;
	}
	
	.arrow-up {
		transform: rotate(180deg);
	}
	
	.faq-answer {
		max-height: 0;
		overflow: hidden;
		transition: max-height 0.3s ease;
	}
	
	.faq-answer.expanded {
		max-height: 1000rpx;
	}
	
	.answer-text {
		padding: 0 32rpx 32rpx;
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.6;
		display: block;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.faq-question {
			padding: 24rpx;
		}
		
		.question-text {
			font-size: 26rpx;
		}
		
		.answer-text {
			padding: 0 24rpx 24rpx;
			font-size: 24rpx;
		}
	}
</style>