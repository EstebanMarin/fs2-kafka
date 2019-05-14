package fs2.kafka

import cats.implicits._
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition

final class CommittableOffsetSpec extends BaseSpec {
  describe("CommittableOffset") {
    it("should be able to commit the offset") {
      val partition = new TopicPartition("topic", 0)
      val offsetAndMetadata = new OffsetAndMetadata(0L, "metadata")
      var committed: Map[TopicPartition, OffsetAndMetadata] = null

      CommittableOffset[Id](
        partition,
        offsetAndMetadata,
        consumerGroupId = None,
        committed = _
      ).commit

      assert(committed == Map(partition -> offsetAndMetadata))
    }

    it("should have a Show instance and matching toString") {
      val partition = new TopicPartition("topic", 0)

      assert {
        val offsetAndMetadata = new OffsetAndMetadata(0L, "metadata")
        val offset = CommittableOffset[Id](partition, offsetAndMetadata, None, _ => ())

        offset.toString == "CommittableOffset(topic-0 -> (0, metadata))" &&
        offset.show == offset.toString
      }

      assert {
        val offsetAndMetadata = new OffsetAndMetadata(0L, "metadata")
        val offset = CommittableOffset[Id](partition, offsetAndMetadata, Some("the-group"), _ => ())

        offset.toString == "CommittableOffset(topic-0 -> (0, metadata), the-group)" &&
        offset.show == offset.toString
      }

      assert {
        val offsetAndMetadata = new OffsetAndMetadata(0L)
        val offset = CommittableOffset[Id](partition, offsetAndMetadata, None, _ => ())

        offset.toString == "CommittableOffset(topic-0 -> 0)" &&
        offset.show == offset.toString
      }

      assert {
        val offsetAndMetadata = new OffsetAndMetadata(0L)
        val offset = CommittableOffset[Id](partition, offsetAndMetadata, Some("the-group"), _ => ())

        offset.toString == "CommittableOffset(topic-0 -> 0, the-group)" &&
        offset.show == offset.toString
      }
    }
  }

}
