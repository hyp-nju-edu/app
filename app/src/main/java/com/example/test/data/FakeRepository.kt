package com.example.test.data

import android.net.Uri
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object FakeRepository {

    private val orders = mutableListOf(
        Order(
            id = "o1",
            title = "西山轻徒步一日",
            startTime = "02-02 08:30",
            from = "地铁A口",
            to = "西山观景台",
            tags = listOf("轻松", "1天", "风景"),
            price = "¥199/人",
            peopleCount = 6,
            status = OrderStatus.AVAILABLE,
            routeImageUrl = null,
            customerName = "小李",
            estimatedDuration = "约4-5小时",
            estimatedStartTime = "08:30",
            estimatedEndTime = "13:30"
        ),
        Order(
            id = "o2",
            title = "城市夜跑路线",
            startTime = "02-01 20:00",
            from = "体育中心",
            to = "江边公园",
            tags = listOf("夜景", "2小时"),
            price = "¥99/人",
            peopleCount = 10,
            status = OrderStatus.TAKEN_BY_OTHER,
            routeImageUrl = null,
            customerName = "小王",
            estimatedDuration = "约2小时",
            estimatedStartTime = "20:00",
            estimatedEndTime = "22:00"
        )
    )

    private val conversations = mutableListOf(
        Conversation(
            id = "c1",
            peerName = "用户小李",
            lastMessage = "领队你好，集合点可以改吗？",
            lastTime = "10:21",
            unreadCount = 2,
            orderId = "o1"
        )
    )

    private val messagesByConv = ConcurrentHashMap<String, MutableList<Message>>().apply {
        put(
            "c1",
            mutableListOf(
                Message("m1", "c1", isMe = false, type = MessageType.TEXT, text = "你好！这个路线大概多久？", time = "10:20"),
                Message("m2", "c1", isMe = true, type = MessageType.TEXT, text = "约 4-5 小时，包含休息拍照。", time = "10:21"),
                Message("m3", "c1", isMe = false, type = MessageType.TEXT, text = "好的，那大概几点开始呢？", time = "10:22"),
                Message("m4", "c1", isMe = true, type = MessageType.TEXT, text = "早上8点集合，8点半准时出发", time = "10:23"),
                Message("m5", "c1", isMe = false, type = MessageType.TEXT, text = "明白了，谢谢！", time = "10:24")
            )
        )
    }

    private val reviews = listOf(
        Review("r1", 5f, "很专业，路线安排合理", listOf("专业", "守时"), "2026-01-20", "用户A"),
        Review("r2", 4.5f, "沟通顺畅，体验不错", listOf("沟通好", "耐心"), "2026-01-18", "用户B"),
        Review("r3", 5f, "非常靠谱，下次还找", listOf("靠谱", "路线熟"), "2026-01-10", "用户C")
    )

    fun listOrders(): List<Order> = orders.toList()

    fun getOrder(orderId: String): Order? = orders.find { it.id == orderId }

    /**
     * 一单一领队：仅允许 AVAILABLE -> ACCEPTED_BY_ME
     */
    fun acceptOrder(orderId: String): Result<Unit> {
        val idx = orders.indexOfFirst { it.id == orderId }
        if (idx == -1) return Result.failure(IllegalArgumentException("订单不存在"))
        val cur = orders[idx]
        return when (cur.status) {
            OrderStatus.AVAILABLE -> {
                orders[idx] = cur.copy(status = OrderStatus.ACCEPTED_BY_ME)
                // 接单成功后，模拟创建/更新会话
                if (conversations.none { it.orderId == orderId }) {
                    conversations.add(
                        0,
                        Conversation(
                            id = "c_${orderId}",
                            peerName = "该订单用户",
                            lastMessage = "你已接单，可以开始沟通",
                            lastTime = "现在",
                            unreadCount = 0,
                            orderId = orderId
                        )
                    )
                    messagesByConv["c_${orderId}"] = mutableListOf(
                        Message(
                            id = "m_${orderId}_sys",
                            conversationId = "c_${orderId}",
                            isMe = false,
                            type = MessageType.TEXT,
                            text = "你好，我是发布者，可以先确认集合细节吗？",
                            time = "现在"
                        )
                    )
                }
                Result.success(Unit)
            }
            OrderStatus.TAKEN_BY_OTHER -> Result.failure(IllegalStateException("已被其他领队接单"))
            OrderStatus.EXPIRED -> Result.failure(IllegalStateException("订单已过期"))
            OrderStatus.ACCEPTED_BY_ME -> Result.success(Unit)
        }
    }

    fun listConversations(): List<Conversation> = conversations.toList()

    fun getConversationByOrder(orderId: String): Conversation? = conversations.find { it.orderId == orderId }

    fun listMessages(conversationId: String): List<Message> =
        messagesByConv[conversationId]?.toList().orEmpty()

    fun sendText(conversationId: String, text: String) {
        val list = messagesByConv.getOrPut(conversationId) { mutableListOf() }
        list.add(
            Message(
                id = "m_${Random.nextInt()}",
                conversationId = conversationId,
                isMe = true,
                type = MessageType.TEXT,
                text = text,
                time = "现在"
            )
        )
        updateConvPreview(conversationId, text)
    }

    fun sendImage(conversationId: String, uri: Uri) {
        val list = messagesByConv.getOrPut(conversationId) { mutableListOf() }
        list.add(
            Message(
                id = "m_${Random.nextInt()}",
                conversationId = conversationId,
                isMe = true,
                type = MessageType.IMAGE,
                imageUri = uri,
                time = "现在"
            )
        )
        updateConvPreview(conversationId, "[图片]")
    }

    private fun updateConvPreview(conversationId: String, preview: String) {
        val idx = conversations.indexOfFirst { it.id == conversationId }
        if (idx != -1) {
            val cur = conversations[idx]
            conversations[idx] = cur.copy(lastMessage = preview, lastTime = "现在")
        }
    }

    fun listReviews(): List<Review> = reviews
}