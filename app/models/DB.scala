package models

// Declare a model
case class Artist(name: String, genres: Set[Genre])
case class Genre(name: String)

// Initialize SORM, automatically generating schema
import sorm._
object Db extends Instance(
  entities = Set( Entity[Artist](), Entity[Genre]() ),
  url = "jdbc:h2:mem:test"
)

object DbDemo {
  def demo(): Unit = {
    // Store values in the db
    val pop = Db.save(Genre("Pop"))
    val folk = Db.save(Genre("Folk"))
    Db.save(Artist("Shake Shake Go", Set(pop, folk)))
    Db.save(Artist("First Aid Kit", Set(folk)))

    // Retrieve values from the db
    // Option[Artist with Persisted]:
    val firstAidKit = Db.query[Artist].whereEqual("name", "First Aid Kit").fetchOne()
    // Stream[Artist with Persisted]:
    val folkArtists = Db.query[Artist].whereEqual("genres.item.name", "Folk").fetch()

    // Use a.copy() for 'Artist with Persisted' to 'Artist' conversion
    // Use a.copy(sth1 = newVal, sth2 = newVal2) to create another instance with some changes
    //  -> Db.save('Artist with Persisted' instance) performs an UPDATE instead of an INSERT operation
    // Use Db.delete[Artist]('Artist with Persisted' instance here - maybe a result of previous queries)
  }
}

