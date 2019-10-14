# concurrent [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
高并发场景下，库存问题

### 解决方案：
#### 1、利用redis string存储库存，有超发的可能
#### 2、利用redis incr/decr原子性操作，无超发的可能，有少发的可能
#### 3、利用redisson分布式锁，无超发的可能，性能比较差
#### 4、利用redis队列结构（list），加仓入列，减仓出列，无超发可能，有少发可能

eg：针对少发的情况，也可弥补
