<template>
	<view class="date-search-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">按日期查找</text>
			<view class="placeholder"></view>
		</view>
		
		<!-- 日期选择控件 -->
		<view class="date-controls">
			<view class="year-month-row">
				<view class="dropdown year-dropdown" @click="openYearPicker">
					<text class="dropdown-text">{{ currentYear }}年</text>
					<view class="dropdown-arrow"></view>
				</view>
				<view class="dropdown month-dropdown" @click="openMonthPicker">
					<text class="dropdown-text">{{ currentMonth }}月</text>
					<view class="dropdown-arrow"></view>
				</view>
			</view>
			<view class="view-toggle">
				<view class="toggle-btn" :class="{ active: viewMode === 'month' }" @click="setViewMode('month')">
					<text class="toggle-text">月</text>
				</view>
				<view class="toggle-btn" :class="{ active: viewMode === 'year' }" @click="setViewMode('year')">
					<text class="toggle-text">年</text>
				</view>
			</view>
		</view>
		
		<!-- 日历网格 -->
		<view class="calendar-grid">
			<!-- 星期标题行 -->
			<view class="weekdays-row">
				<view class="weekday" v-for="day in weekdays" :key="day">
					<text class="weekday-text">{{ day }}</text>
				</view>
			</view>
			
			<!-- 日期网格 -->
			<view class="dates-grid">
				<view 
					class="date-cell" 
					v-for="(date, index) in calendarDates" 
					:key="index"
					:class="{
						'other-month': date.otherMonth,
						'current-month': !date.otherMonth,
						'selected': date.selected,
						'today': date.isToday
					}"
					@click="selectDate(date)"
				>
					<text class="date-number">{{ date.day }}</text>
					<view v-if="date.selected" class="selected-dot"></view>
				</view>
			</view>
		</view>
		
		<!-- 年份选择弹层 -->
		<view class="year-picker-overlay" v-if="showYearPicker" @click="hideYearPicker">
			<view class="year-picker-sheet" @click.stop>
				<view class="year-picker-content">
					<view class="year-range">
						<view class="year-item" 
							v-for="year in yearRange" 
							:key="year"
							:class="{ active: year === currentYear }"
							@click="selectYear(year)"
						>
							<text class="year-text">{{ year }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 月份选择弹层 -->
		<view class="month-picker-overlay" v-if="showMonthPicker" @click="hideMonthPicker">
			<view class="month-picker-sheet" @click.stop>
				<view class="month-picker-content">
					<view class="month-grid">
						<view class="month-item" 
							v-for="month in 12" 
							:key="month"
							:class="{ active: month === currentMonth }"
							@click="selectMonth(month)"
						>
							<text class="month-text">{{ month }}月</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'DateSearchPage',
		data() {
			return {
				currentYear: 2021,
				currentMonth: 8,
				selectedDate: null,
				viewMode: 'month', // 'month' 或 'year'
				showYearPicker: false,
				showMonthPicker: false,
				weekdays: ['一', '二', '三', '四', '五', '六', '七']
			}
		},
		computed: {
			yearRange() {
				const years = [];
				for (let i = 2020; i <= 2025; i++) {
					years.push(i);
				}
				return years;
			},
			calendarDates() {
				const dates = [];
				const firstDay = new Date(this.currentYear, this.currentMonth - 1, 1);
				const lastDay = new Date(this.currentYear, this.currentMonth, 0);
				const startDate = new Date(firstDay);
				startDate.setDate(startDate.getDate() - firstDay.getDay() + 1);
				
				// 添加上个月的日期
				for (let i = 0; i < firstDay.getDay() - 1; i++) {
					const prevDate = new Date(startDate);
					prevDate.setDate(prevDate.getDate() + i);
					dates.push({
						day: prevDate.getDate(),
						otherMonth: true,
						selected: false,
						isToday: false,
						fullDate: prevDate
					});
				}
				
				// 添加当前月的日期
				for (let i = 1; i <= lastDay.getDate(); i++) {
					const currentDate = new Date(this.currentYear, this.currentMonth - 1, i);
					const isToday = this.isToday(currentDate);
					const isSelected = this.selectedDate && this.isSameDate(currentDate, this.selectedDate);
					
					dates.push({
						day: i,
						otherMonth: false,
						selected: isSelected,
						isToday: isToday,
						fullDate: currentDate
					});
				}
				
				// 添加下个月的日期
				const remainingCells = 42 - dates.length; // 6行7列 = 42
				for (let i = 1; i <= remainingCells; i++) {
					const nextDate = new Date(this.currentYear, this.currentMonth, i);
					dates.push({
						day: i,
						otherMonth: true,
						selected: false,
						isToday: false,
						fullDate: nextDate
					});
				}
				
				return dates;
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			setViewMode(mode) {
				this.viewMode = mode;
			},
			openYearPicker() {
				this.showYearPicker = true;
			},
			hideYearPicker() {
				this.showYearPicker = false;
			},
			openMonthPicker() {
				this.showMonthPicker = true;
			},
			hideMonthPicker() {
				this.showMonthPicker = false;
			},
			selectYear(year) {
				this.currentYear = year;
				this.showYearPicker = false;
			},
			selectMonth(month) {
				this.currentMonth = month;
				this.showMonthPicker = false;
			},
			selectDate(date) {
				if (!date.otherMonth) {
					this.selectedDate = date.fullDate;
					// 可以在这里添加选择日期后的逻辑
					uni.showToast({
						title: `已选择: ${date.fullDate.getFullYear()}-${(date.fullDate.getMonth() + 1).toString().padStart(2, '0')}-${date.fullDate.getDate().toString().padStart(2, '0')}`,
						icon: 'none'
					});
				}
			},
			isToday(date) {
				const today = new Date();
				return this.isSameDate(date, today);
			},
			isSameDate(date1, date2) {
				return date1.getFullYear() === date2.getFullYear() &&
					   date1.getMonth() === date2.getMonth() &&
					   date1.getDate() === date2.getDate();
			}
		}
	}
</script>

<style>
	.date-search-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: #FFFFFF;
	}
	
	.time {
		font-size: 28rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.signal, .wifi, .battery {
		width: 24rpx;
		height: 24rpx;
		background-color: #000000;
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
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
		border-right: 12rpx solid #000000;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 日期选择控件 */
	.date-controls {
		padding: 24rpx 32rpx;
		background-color: #F8F8F8;
		margin: 0 32rpx;
		border-radius: 16rpx;
		margin-top: 24rpx;
	}
	
	.year-month-row {
		display: flex;
		gap: 24rpx;
		margin-bottom: 16rpx;
	}
	
	.dropdown {
		display: flex;
		align-items: center;
		gap: 8rpx;
		padding: 12rpx 16rpx;
		background-color: #FFFFFF;
		border-radius: 12rpx;
		border: 1rpx solid #E0E0E0;
	}
	
	.dropdown-text {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.dropdown-arrow {
		width: 0;
		height: 0;
		border-top: 8rpx solid #666666;
		border-left: 6rpx solid transparent;
		border-right: 6rpx solid transparent;
	}
	
	.view-toggle {
		display: flex;
		background-color: #FFFFFF;
		border-radius: 12rpx;
		padding: 4rpx;
		border: 1rpx solid #E0E0E0;
	}
	
	.toggle-btn {
		flex: 1;
		padding: 12rpx 0;
		text-align: center;
		border-radius: 8rpx;
		transition: all 0.2s ease;
	}
	
	.toggle-btn.active {
		background-color: #007AFF;
	}
	
	.toggle-text {
		font-size: 26rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.toggle-btn.active .toggle-text {
		color: #FFFFFF;
	}
	
	/* 日历网格 */
	.calendar-grid {
		padding: 32rpx;
		flex: 1;
	}
	
	.weekdays-row {
		display: flex;
		margin-bottom: 24rpx;
	}
	
	.weekday {
		flex: 1;
		text-align: center;
		padding: 16rpx 0;
	}
	
	.weekday-text {
		font-size: 26rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.dates-grid {
		display: grid;
		grid-template-columns: repeat(7, 1fr);
		gap: 8rpx;
	}
	
	.date-cell {
		aspect-ratio: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		border-radius: 12rpx;
		position: relative;
		cursor: pointer;
	}
	
	.date-cell.other-month {
		color: #CCCCCC;
	}
	
	.date-cell.current-month {
		color: #333333;
	}
	
	.date-cell.selected {
		background-color: #007AFF;
		color: #FFFFFF;
	}
	
	.date-cell.today {
		background-color: #F0F8FF;
		border: 2rpx solid #007AFF;
	}
	
	.date-number {
		font-size: 28rpx;
		font-weight: 500;
	}
	
	.selected-dot {
		width: 8rpx;
		height: 8rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
		margin-top: 4rpx;
	}
	
	/* 年份选择弹层 */
	.year-picker-overlay {
		position: fixed;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.4);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 999;
	}
	
	.year-picker-sheet {
		width: 500rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
	}
	
	.year-picker-content {
		max-height: 400rpx;
		overflow-y: auto;
	}
	
	.year-range {
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}
	
	.year-item {
		padding: 20rpx;
		text-align: center;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.year-item.active {
		background-color: #007AFF;
	}
	
	.year-item:active {
		background-color: #F0F0F0;
	}
	
	.year-item.active:active {
		background-color: #0056CC;
	}
	
	.year-text {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.year-item.active .year-text {
		color: #FFFFFF;
	}
	
	/* 月份选择弹层 */
	.month-picker-overlay {
		position: fixed;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.4);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 999;
	}
	
	.month-picker-sheet {
		width: 500rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
	}
	
	.month-picker-content {
		max-height: 400rpx;
		overflow-y: auto;
	}
	
	.month-grid {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 16rpx;
	}
	
	.month-item {
		padding: 20rpx;
		text-align: center;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.month-item.active {
		background-color: #007AFF;
	}
	
	.month-item:active {
		background-color: #F0F0F0;
	}
	
	.month-item.active:active {
		background-color: #0056CC;
	}
	
	.month-text {
		font-size: 26rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.month-item.active .month-text {
		color: #FFFFFF;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
</style>

