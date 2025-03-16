

<template>
  <div class="ly-header">

    <div class="ly-header-banner">
      <img src="/banner.png">
    </div>

    <div class="ly-header-tag">
      <span>首页</span>
      <span>文章</span>
    </div>

    <div class="ly-header-search">
      <button>搜索</button>
    </div>
  </div>

</template>

<script setup>

</script>

<style lang="scss"scoped>
  .ly-header{
    display: flex;

    .ly-header-banner {
      display: block;
      width: 150px;
      margin-left: 100px;
      background-color: #535bf2;
    }

    .ly-header-tag {
      display: block;
      width: 200px;
      background-color: #535bf2;
    }

    .ly-header-search {
      display: block;
    }
  }

</style>
