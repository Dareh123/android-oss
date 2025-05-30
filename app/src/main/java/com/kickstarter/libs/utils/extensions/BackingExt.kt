@file:JvmName("BackingExt")
package com.kickstarter.libs.utils.extensions

import com.kickstarter.libs.utils.RewardUtils.isNoReward
import com.kickstarter.libs.utils.RewardUtils.isShippable
import com.kickstarter.models.Backing
import com.kickstarter.models.Order
import com.kickstarter.models.Project
import com.kickstarter.models.Reward

fun Backing.isBacked(reward: Reward): Boolean {
    val rewardId = this.rewardId() ?: return isNoReward(reward)
    return rewardId == reward.id()
}

fun Backing.isErrored(): Boolean = this.status() == Backing.STATUS_ERRORED

fun Backing.isErroredWithPLOT(): Boolean = (this.status() == Backing.STATUS_AUTHENTICATION_REQUIRED || this.status() == Backing.STATUS_ERRORED) && !this.paymentIncrements().isNullOrEmpty()

fun Backing.isShippable(): Boolean {
    val reward = this.reward() ?: return false
    return isShippable(reward)
}

fun Backing.isOrderPresentAndComplete(): Boolean {
    return this.order().isNotNull() && this.order()?.checkoutState() == Order.CheckoutStateEnum.COMPLETE
}

fun Backing.backedReward(project: Project): Reward? {
    val rewards = project.rewards() ?: return null
    for (reward in rewards) {
        if (isBacked(reward)) {
            return reward
        }
    }
    return null
}
