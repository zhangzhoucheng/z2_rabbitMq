## 基础概念
### 消费者
### 生产者
### 交换机

## 交换机的消息发布模式
如：direct, topic, headers and fanout 表示 直接、主题、标题和扇出（即广播）
用法：channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

## 消息传递模型
- 基础 点对点
- work queue 一对多
- 发布订阅模型
- 路由模型
- 主题模型
- rpc模式

## 消息模型详述
### 基础 点对点
由生产者，队列，消费者组成；最基础的模型
### work queue一对多
由生产者，队列，消费者组成；但是较于点对点 有更多的消费者，且可以指定消费能力（如channel.basicQos(1);),消息持久化配置，以及消息回调确认ack；
### 发布订阅模型
生产者生产的消息通过设定的模式（如例子中的广播模式faout)发布到交换机后（可以指定一个RoutingKey=""），所有的队列可以进行绑定匹配订阅，进而消费；（exchange 设置=direct）
### 路由模型
基于订阅模型。一般设置exchangeDeclare->direct。exchange可以发布routingKey不同的订阅消息。这时候消费者 的channel.queueBind（）可以给queue配置一个或多个routingKey，用来接收对应routingKey的消息，进行订阅。（exchange 设置=direct）
关键：在于一个交换机可以定义多个routeKey，一个routeKey可以被多个队列绑定，一个队列可以绑定多个routeKey；
### 主题模型
 在routing模型基础上，给routingKey赋值   *.*.* 的模式，从而模糊匹配到 对应的queue，可以完成多条件匹配（即消费者的一个 routingKey 'lazy.#'
可以匹配lazy开头主题消息。当然一个消费者channele queue可以配置多个routingKey，一个routingKey完成模糊匹配，或者直接匹配，很灵活)（exchange 设置=topic）

### rpc模式
由生产者 生产消息到队列A，且传递回调的队列名 replyQueueName；消费者监听A,消费消息，且返回消息到replyQueueName，且生产者会监听这个replyQueueName，从而生产者消费这个回调消息；这样
生产者就调用了 消费者这个远程服务；完成rpc调用；
特点：代码繁杂，不易维护，且采用阻塞监听；当然如果处理好这些问题，其效率是很快的；有时候可以通过异步管道等代替rpc，如果时效性要求不高；