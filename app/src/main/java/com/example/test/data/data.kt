package com.example.test.data

import android.net.Uri

enum class OrderStatus { AVAILABLE, TAKEN_BY_OTHER, EXPIRED, ACCEPTED_BY_ME }

data class Order(
    val id: String,
    val title: String,
    val startTime: String,
    val from: String,
    val to: String,
    val tags: List<String>,
    val price: String,
    val peopleCount: Int,
    val status: OrderStatus,
    val routeImageUrl: String? = null, // 先用图片占位（url 或 null）
    val customerName: String = "", // 客户姓名
    val estimatedDuration: String = "", // 预估用时，如"4-5小时"
    val estimatedStartTime: String = "", // 预估开始时间
    val estimatedEndTime: String = "" // 预估结束时间
)

data class Conversation(
    val id: String,
    val peerName: String,
    val peerAvatarUrl: String? = null,
    val lastMessage: String,
    val lastTime: String,
    val unreadCount: Int,
    val orderId: String
)

enum class MessageType { TEXT, IMAGE, AUDIO }

data class Message(
    val id: String,
    val conversationId: String,
    val isMe: Boolean,
    val type: MessageType,
    val text: String? = null,
    val imageUri: Uri? = null,
    val audioPath: String? = null,
    val audioDurationSec: Int? = null,
    val time: String
)

data class Review(
    val id: String,
    val rating: Float,
    val content: String,
    val tags: List<String>,
    val time: String,
    val fromUserName: String
)