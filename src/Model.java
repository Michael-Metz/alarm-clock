import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Singleton class that handles on IO of all the alarms.
 */
public class Model {

    private static Model singletonModel;

    private ArrayList<Alarm> alarms;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String xmlFilePath = "alarms.xml";

    private Model() {
        alarms = new ArrayList<>(6);
        readXml();
    }

    /**
     * Returns the single instance of the model
     *
     * @return single instance of model
     */
    public static Model getInstance() {
        if (singletonModel == null) {
            singletonModel = new Model();
        }
        return singletonModel;
    }

    public void addAlarm(Alarm alarm){
        alarms.add(alarm);
        save();
    }
    public void removeAlarm(Alarm alarm){
        alarms.remove(alarm);
        save();
    }
    public void save(){
        try{
            writeXml();
        }catch (Exception e){
            System.out.println("can't write xml");
        }
    }
    public ArrayList<Alarm> getAlarms(){
        return alarms;
    }


    //Input Output methods below


    /**
     * Writes the current contents of the alarms array to disk @ alarms.xml
     *
     * @throws Exception
     */
    private void writeXml() throws Exception {
        // create an XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        // create XMLEventWriter
        XMLEventWriter eventWriter = outputFactory
                .createXMLEventWriter(new FileOutputStream(xmlFilePath));
        // create an EventFactory
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent newLineCode = eventFactory.createDTD("\n");
        XMLEvent tabCode = eventFactory.createDTD("\t");

        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        // create alarms open tag
        StartElement alarmsStartElement = eventFactory.createStartElement("",
                "", "alarms");
        EndElement alarmsEndElement = eventFactory.createEndElement("", "", "alarms");
        eventWriter.add(newLineCode);
        eventWriter.add(alarmsStartElement);
        eventWriter.add(newLineCode);

        //add all alarms
        StartElement alarmStartElement = eventFactory.createStartElement("", "", "alarm");
        EndElement alarmEndElement = eventFactory.createEndElement("", "", "alarms");


        for (Alarm a : alarms) {
            eventWriter.add(tabCode);
            eventWriter.add(alarmStartElement);
            eventWriter.add(newLineCode);

            String date = dateFormat.format(a.getDate());
            createNode(eventWriter, "date", date);
            createNode(eventWriter, "message", a.getMessage());
            createNode(eventWriter, "snoozecount", Integer.toString(a.getSnoozeCount()));

            eventWriter.add(tabCode);
            eventWriter.add(alarmEndElement);
            eventWriter.add(newLineCode);
        }


        eventWriter.add(alarmsEndElement);
        eventWriter.add(newLineCode);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }

    /**
     * Read alarms from xml file into the alarms array list.
     * this creates the alarms thus starting their timers.
     */
    private void readXml() {
        boolean bDate = false;
        boolean bMessage = false;
        boolean bSnoozeCount = false;

        String date = null;
        String message = null;
        String snoozecount = null;

        try
        {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(new FileReader(xmlFilePath));

            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType())
                {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();

                        if (qName.equalsIgnoreCase("alarm")) {
                            System.out.println("Start Element : alarm");
                        } else if (qName.equalsIgnoreCase("date")) {
                            bDate = true;
                        } else if (qName.equalsIgnoreCase("message")) {
                            bMessage = true;
                        } else if (qName.equalsIgnoreCase("snoozecount")) {
                            bSnoozeCount = true;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (bDate) {
                            date = characters.getData();
                            bDate = false;
                        }
                        if (bMessage) {
                            message = characters.getData();
                            bMessage = false;
                        }
                        if (bSnoozeCount) {
                            snoozecount = characters.getData();
                            bSnoozeCount = false;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();

                        if (endElement.getName().getLocalPart().equalsIgnoreCase("alarm")) {

                            //we got all we need to add a alarm to our file
                            if (date != null && snoozecount != null && message != null) {
                                try {
                                    Date d = dateFormat.parse(date);
                                    int sc = Integer.parseInt(snoozecount);
                                    alarms.add(new Alarm(d, message, sc));
                                } catch (ParseException e) {
                                    System.out.println("date has been messed with" + date);
                                }

                            }

                            //reset to null
                            date = snoozecount = message = null;
                        }
                        break;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("we dont have a xml file that can be read");
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }
    }
}
